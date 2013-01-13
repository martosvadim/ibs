package edu.ibs.core.operation.logic;

import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.enums.CardBookType;
import edu.ibs.common.enums.Period;
import edu.ibs.common.enums.ProviderField;
import edu.ibs.core.controller.SpecifiedJpaController;
import edu.ibs.core.controller.exception.FreezedException;
import edu.ibs.core.controller.exception.NotEnoughMoneyException;
import edu.ibs.core.entity.*;
import edu.ibs.core.operation.AdminOperations;
import edu.ibs.core.operation.UserOperations;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public final class CommonService implements UserOperations, AdminOperations {

	public static final String VALID_EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEXP);
	private final SpecifiedJpaController dataSource = SpecifiedJpaController.instance();
	private final Logger log = Logger.getLogger(CommonService.class);

	@Override
	public void update(Account acc) {
		if (dataSource.exist(Account.class, acc.getId())) {
			if (acc.getUser() != null) {
				if (acc.getUser().getId() == 0) {
					dataSource.insert(acc.getUser());
				} else {
					dataSource.update(acc.getUser());
				}
			}
			dataSource.update(acc);
		} else {
			throw new IllegalArgumentException("Account does not exists");
		}
	}

	@Override
	public List<BankBook> getBankBooks(User user) {
		return dataSource.getBankBooksOf(user);
	}

	@Override
	public List<CardBook> getCardBooks(User user) {
		if (user == null) {
			throw new NullPointerException();
		}
		return dataSource.getCardBooksOf(user);
	}

	@Override
	public List<CardBook> getCardBooks(User user, BankBook book, CardBookType type) {
		if (user == null || book == null || type == null) {
			throw new NullPointerException();
		}
		return dataSource.getCardBooksOf(user, book, type);
	}

	@Override
	public List<CardBook> getCardBooks(User user, BankBook book) {
		if (user == null || book == null) {
			throw new NullPointerException();
		}
		return dataSource.getCardBooksOf(user, book);
	}

	@Override
	public List<CardBook> getCardBooks(User user, CardBookType type) {
		if (user == null || type == null) {
			throw new NullPointerException();
		}
		return dataSource.getCardBooksOf(user, type);
	}

	@Override
	public List<CardBook> getCardBooks(BankBook bankBook) {
		if (bankBook == null) {
			throw new NullPointerException();
		}
		return dataSource.getCardBooksOf(bankBook);
	}

	@Override
	public List<CardBook> getCardBooks(BankBook bankBook, CardBookType type) {
		if (bankBook == null || type == null) {
			throw new NullPointerException();
		}
		return dataSource.getCardBooksOf(bankBook, type);
	}

	@Override
	public void update(CardBook cardBook) {
		if (!dataSource.exist(cardBook.getClass(), cardBook.getId())) {
			throw new IllegalArgumentException(String.format("Card book %s does not exist", cardBook));
		}
		dataSource.update(cardBook);
	}

	@Override
	public void reassign(CardBook cardBook, String toUserWithEmail) {
		if (!isValid(toUserWithEmail)) {
			throw new IllegalArgumentException(String.format("Invalid email %s", toUserWithEmail));
		} else if (!dataSource.exist(cardBook.getClass(), cardBook.getId())) {
			throw new IllegalArgumentException(String.format("Card book %s does not exist", cardBook));
		} else if (!checkIfReassignmentIsAvailable(toUserWithEmail)) {
			throw new IllegalArgumentException(String.format("No active user with account's email %s found", toUserWithEmail));
		} else {
			User u = dataSource.getUserByEmail(toUserWithEmail);
			cardBook.setOwner(u);
			dataSource.update(cardBook);
		}
	}

	@Override
	public boolean checkIfReassignmentIsAvailable(String toUserWithEmail) {
		return dataSource.userExists(toUserWithEmail);
	}

	@Override
	public Transaction pay(CardBook from, long toCardBookID, Money money, TransactionType type, String description) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException {
		if (!dataSource.exist(money.currency().getClass(), money.currency().getId())) {
			throw new IllegalArgumentException("Данной валюты не существует");
		}
		CardBook to = dataSource.select(CardBook.class, toCardBookID);
		if (to == null) {
			throw new IllegalArgumentException(String.format("Карт-счета с номером %s не существует", toCardBookID));
		} else if (dataSource.isBookOfProvider(to) && type.equals(TransactionType.TRANSFER)) {
			throw new IllegalArgumentException(String.format("Карт-счет %s является счетом поставщика услуг, невозможно выполнить перевод", to));
		}
		return dataSource.pay(from, to, money, type, description);
	}

	@Override
	public Transaction pay(SavedPayment savedPayment, Money money, String description) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException {
		CardBook from = savedPayment.getTransaction().getFrom();
		CardBook to = savedPayment.getTransaction().getTo();
		TransactionType type = savedPayment.getTransaction().getType();
		if (dataSource.isBookOfProvider(to) && type.equals(TransactionType.TRANSFER)) {
			throw new IllegalArgumentException(String.format("Карт-счет %s является счетом поставщика услуг, невозможно выполнить перевод", to));
		}
		if (!dataSource.exist(money.currency().getClass(), money.currency().getId())) {
			throw new IllegalArgumentException("Данной валюты не существует");
		}
		return dataSource.pay(from, to, money, type, description);
	}

	@Override
	public SavedPayment savePayment(Transaction transaction, User owner) {
		SavedPayment sp = new SavedPayment(transaction, owner);
		dataSource.insert(sp);
		return sp;
	}

	@Override
	public List<SavedPayment> getSavedPayments(User user) {
		return dataSource.getSavedPayments(user);
	}

	@Override
	public void delete(SavedPayment payment) {
		dataSource.delete(payment.getClass(), payment.getId());
	}

	@Override
	public boolean isValid(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	@Override
	public Account login(String email, String passwd) {
		return dataSource.getUserAccount(email, passwd);
	}

	@Override
	public Account register(String email, String passwd) {
		return createAccount(AccountRole.USER, email, passwd);
	}

	@Override
	public boolean isFree(String email) {
		if (isValid(email)) {
			return !dataSource.accountExists(email);
		} else {
			return false;
		}
	}

	@Override
	public Currency getCurrency(String name) {
		return dataSource.getCurrency(name);
	}

	@Override
	public List<Currency> getCurrencies() {
		return dataSource.selectAll(Currency.class);
	}

	@Override
	public List<CreditPlan> getCreditPlans() {
		return dataSource.getActualCreditPlans();
	}

	@Override
	public Account createAccount(AccountRole role, String email, String passwd) {
		if (isValid(email)) {
			if (isFree(email)) {
				Account acc = new Account(email, passwd, role);
				dataSource.insert(acc);
				return acc;
			} else {
				throw new IllegalArgumentException(String.format("Email %s is not free", email));
			}
		} else {
			throw new IllegalArgumentException(String.format("Invalid email %s", email));
		}
	}

	@Override
	public BankBook createBankBook(User user, Money money) {
		if (!dataSource.exist(user.getClass(), user.getId())) {
			throw new IllegalArgumentException("User does not exist");
		}
		if (!dataSource.exist(money.currency().getClass(), money.currency().getId())) {
			throw new IllegalArgumentException("Currency does not exsist");
		}
		BankBook bankBook = new BankBook(user, money);
		dataSource.insert(bankBook);
		return bankBook;

	}

	@Override
	public CardBook createDebitCardBook(User user, BankBook bankBook) {
		return dataSource.createCardBook(user, bankBook, null);
	}

	@Override
	public CardBook createCreditCardBook(User user, BankBook bankBook, CreditPlan credit) {
		if (credit == null) {
			throw new NullPointerException("Credit plan can't be null for credit card");
		}
		return dataSource.createCardBook(user, bankBook, credit);
	}

	@Override
	public boolean addMoney(BankBook bankBook, Money money) throws IllegalArgumentException, FreezedException {
		dataSource.addMoney(bankBook, money);
		return true;
	}

	@Override
	public CreditPlan createCreditPlan(String name, Money limit, Period period, int periodMultiply, int percent) {
		if (!dataSource.exist(limit.currency().getClass(), limit.currency().getId())) {
			throw new IllegalArgumentException("Currency does not exsist");
		}
		CreditPlan plan = new CreditPlan(name, limit, period, periodMultiply, percent);
		dataSource.insert(plan);
		return plan;
	}

	@Override
	public void delete(User user) {
		dataSource.delete(user.getClass(), user.getId());
	}

	@Override
	public void update(List<Currency> currencies) {
		dataSource.batchUpdate(currencies);
	}

	@Override
	public void rollback(Transaction transaction) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException {
		dataSource.rollback(transaction);
	}

	@Override
	public List<CardRequest> getAllRequests() {
		return getRequests(null, null);
	}

	@Override
	public List<CardRequest> getRequests(Date from, Date to) {
		if ((from == null && to != null) || (from != null && to == null)) {
			throw new NullPointerException();
		}
		return dataSource.getCardRequests(from, to);
	}

	@Override
	public CreditPlan getCreditPlan(String name) {
		return dataSource.getCreditPlan(name);
	}

	@Override
	public boolean bankBookExists(long id) {
		return dataSource.exist(BankBook.class, id);
	}

	@Override
	public boolean cardBookExists(long id) {
		return dataSource.exist(CardBook.class, id);
	}

	@Override
	public CardRequest requestDebitCard(User user, BankBook bankBook) throws IllegalArgumentException {
		return dataSource.requestCard(user, bankBook, null);
	}

	@Override
	public CardRequest requestCreditCard(User user, BankBook bankBook, CreditPlan plan) throws IllegalArgumentException {
		if (plan == null) {
			throw new NullPointerException("Credit plan is null");
		}
		return dataSource.requestCard(user, bankBook, plan);
	}

	@Override
	public List<CardRequest> getCardRequestsOf(User user, boolean watched) {
		return dataSource.getCardRequestsOf(user, watched);
	}

	@Override
	public CardBook approve(CardRequest request) {
		return dataSource.processCardRequest(request, true, null);
	}

	@Override
	public void decline(CardRequest request, String reason) {
		dataSource.processCardRequest(request, false, reason);
	}

	@Override
	public BankBook getBankBook(Account user, long id) {
		user = dataSource.select(user.getClass(), user.getId());
		BankBook book = dataSource.select(BankBook.class, id);
		if (book == null) {
			return book;
		} else if (user.getRole().equals(AccountRole.ADMIN) || (user.getUser() != null && user.getUser().equals(book.getOwner()))) {
			return book;
		} else {
			throw new IllegalArgumentException(String.format("User %s doesn't own bank book %s", user, book));
		}
	}

	@Override
	public User getUser(long id) {
		return dataSource.select(User.class, id);
	}

	@Override
	public List<CreditPlan> getCreditPlansFor(Currency curr) {
		return dataSource.getCreditPlansFor(curr);
	}

	@Override
	public void freeze(CreditPlan plan) {
		plan.setFreezed(true);
		dataSource.update(plan);
	}

	@Override
	public void unfreeze(CreditPlan plan) {
		plan.setFreezed(false);
		dataSource.update(plan);
	}

	@Override
	public void freeze(CardBook book) {
		book.setFreezed(true);
		dataSource.update(book);
	}

	@Override
	public void unfreeze(CardBook book) {
		book.setFreezed(false);
		dataSource.update(book);
	}

	@Override
	public void freeze(BankBook book) {
		book.setFreezed(true);
		dataSource.update(book);
	}

	@Override
	public void unfreeze(BankBook book) {
		book.setFreezed(false);
		dataSource.update(book);
	}

	@Override
	public void freeze(User user) {
		user.freeze();
		dataSource.update(user);
	}

	@Override
	public void unfreeze(User user) {
		user.unfreeze();
		dataSource.update(user);
	}

	@Override
	public Autopay createAutopay(CardBook from, CardBook to, Money money, long period) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Autopay createAutopay(Transaction tr, Money money, long period) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Autopay createAutopay(Transaction tr, long period) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Autopay createAutopay(SavedPayment payment, Money money, long period) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Autopay createAutopay(SavedPayment payment, long period) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Autopay> getAutopaysOf(User owner) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public User getUser(String email) {
		if (!isValid(email)) {
			throw new IllegalArgumentException(String.format("Email %s is not valid", email));
		} else {
			return dataSource.getUserByEmail(email);
		}
	}

	@Override
	public List<Provider> getProviderList() {
		return dataSource.selectAll(Provider.class);
	}

	@Override
	public List<Transaction> getAllHistory(User user) {
		return dataSource.getAllHistory(user);
	}

	@Override
	public List<Transaction> getAllHistory(User user, Date from, Date to) {
		if (to.compareTo(from) < 0) {
			throw new IllegalArgumentException(String.format("Date from [%s] is after date to [%s]", from, to));
		}
		return dataSource.getAllHistory(user, from, to);
	}

	@Override
	public void update(User user) {
		dataSource.update(user);
	}

	@Override
	public User getUserByPassport(String passportNumber) {
		if (!User.validatePassportNumber(passportNumber)) {
			throw new IllegalArgumentException(String.format("Passport number %s is not valid", passportNumber));
		} else {
			return dataSource.getUserByPassportNumber(passportNumber);
		}
	}

	@Override
	public Provider createProvider(String company, String bookDescription, Currency currency, ProviderField... fields) {
		return dataSource.createProvider(company, bookDescription, currency, fields);
	}

	@Override
	public void deleteProvider(Provider provider) {
		dataSource.delete(Provider.class, provider.getId());
	}

	@Override
	public <T extends AbstractEntity> List<T> selectEntities(Class<T> clazz, int from, int to) {
		if (from < 0) {
			throw new IllegalArgumentException(String.format("Параметр [from - %s] должен быть положительным", from));
		} else if (to < 0) {
			throw new IllegalArgumentException(String.format("Параметр [to - %s] должен быть положительным", to));
		} else if (from > to) {
			throw new IllegalArgumentException(String.format("Параметр [from - %s] больше параметра [to - %s]", from, to));
		} else if (to == 0) {
			return new LinkedList<T>();
		}
		return dataSource.selectAll(clazz, to - from, from);
	}

	@Override
	public <T extends AbstractEntity> int count(Class<T> clazz) {
		return dataSource.count(clazz);
	}

	@Override
	public boolean cardBookExist(long cardBookID) {
		return dataSource.exist(CardBook.class, cardBookID);
	}
}
