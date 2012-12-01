package edu.ibs.core.service.logic;

import edu.ibs.core.controller.SpecifiedJpaController;
import edu.ibs.core.controller.exceptions.NonexistentEntityException;
import edu.ibs.core.entity.Transaction.TransactionType;
import edu.ibs.core.entity.*;
import edu.ibs.core.service.AdminServicable;
import edu.ibs.core.service.UserServicable;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public final class CommonService implements UserServicable, AdminServicable {

	public static final String VALID_EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEXP);
	private final SpecifiedJpaController source = SpecifiedJpaController.instance();
	private final Logger log = Logger.getLogger(CommonService.class);

	@Override
	public User create(User.Role role, String email, String passwd) throws IllegalArgumentException {
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException(String.format("Invalid email: '%s'", email));
		} else {
			User u = new User(role, email, passwd);
			source.insert(u);
			return u;
		}
	}

	@Override
	public void update(User user) {
		try {
			source.update(user);
		} catch (NonexistentEntityException ex) {
			log.error(ex);
		} catch (Exception ex) {
			log.error(ex);
		}
	}

	@Override
	public List<BankBook> getBankBooks(User user) {
		return source.bankBooks(user);
	}

	@Override
	public Transaction transfer(BankBook from, BankBook to, Money money, TransactionType type) {
		return source.pay(from, to, money, type);
	}

	@Override
	public SavedPayment savePayment(Transaction transaction, User owner) {
		SavedPayment sp = new SavedPayment(transaction, owner);
		source.insert(sp);
		return sp;
	}

	@Override
	public List<SavedPayment> getSavedPayments(User user) {
		return source.savedPayments(user);
	}

	@Override
	public List<Transaction> getHistory(User user, TransactionType type) {
		return source.history(user, type);
	}

	@Override
	public void delete(SavedPayment payment) {
		try {
			source.delete(SavedPayment.class, payment.getId());
		} catch (NonexistentEntityException ex) {
			log.error(ex);
		}
	}

	@Override
	public User getUser(String email, String passwd) {
		return source.user(email, passwd);
	}

	@Override
	public Currency getCurrency(String name) {
		return source.currency(name);
	}

	@Override
	public List<Currency> getCurrencies() {
		return source.selectAll(Currency.class);
	}

	@Override
	public BankBook create(User user, Money money) {
		BankBook book = new BankBook(money, user, false);
		source.insert(book);
		return book;
	}

	@Override
	public boolean addMoney(BankBook bankBook, Money money) {
		if (bankBook.isFreezed() || !bankBook.getCurrency().equals(money.currency())) {
			return false;
		}
		source.addMoney(bankBook, money);
		return true;
	}

	@Override
	public void delete(User user) {
		try {
			source.delete(User.class, user.getId());
		} catch (NonexistentEntityException ex) {
			log.error(String.format("User with id %s doesn't exist", user.getId()), ex);
		}
	}

	@Override
	public void update(List<Currency> currencies) {
		source.batchUpdate(currencies);
	}

	@Override
	public void rollback(Transaction transaction) {
		source.rollback(transaction);
	}

	@Override
	public List<Request> getAllRequests() {
		return source.selectAll(Request.class);
	}

	@Override
	public List<Request> getRequests(Date from, Date to) {
		return source.requests(from, to);
	}
}
