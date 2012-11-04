package edu.ibs.core.service;

import edu.ibs.core.controller.SpecifiedJpaController;
import edu.ibs.core.controller.exceptions.NonexistentEntityException;
import edu.ibs.core.entity.Transaction.TransactionType;
import edu.ibs.core.entity.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
final class CommonService implements UserServicable, AdminServicable {

	public static final String VALID_EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEXP);
	private final SpecifiedJpaController source = SpecifiedJpaController.instance();
	private final Logger log = Logger.getLogger(CommonService.class);

	@Override
	public User create(User.Role role, String email, String passwd) {
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException(String.format("Invalid email: '%s'", email));
		} else {
			User u = new User(role, email);
			source.insert(u);
			return u;
		}
	}

	@Override
	public boolean update(User user) {
		try {
			source.update(user);
			return true;
		} catch (NonexistentEntityException ex) {
			log.error(String.format("User %s doesn't exist", user), ex);
		} catch (Exception ex) {
			log.error(null, ex);
		}
		return false;
	}

	@Override
	public List<BankBook> getBankBooks(User user) {
		return source.bankBooks(user);
	}

	@Override
	public Transaction transfer(BankBook from, BankBook to, TransactionType type, long amount) {
		return source.pay(from, to, amount, type);
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
	public boolean delete(SavedPayment payment) {
		try {
			source.delete(SavedPayment.class, payment.getId());
			return true;
		} catch (NonexistentEntityException ex) {
			log.error(String.format("Payment %s doesn't exist", payment), ex);
		}
		return false;
	}

	@Override
	public User getUser(String email, String passwd) {
		return null;
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
	public BankBook create(User user, Currency currency, long balance) {
		BankBook book = new BankBook(balance, false, currency, user);
		source.insert(book);
		return book;
	}

	@Override
	public boolean addMoney(BankBook bankBook, long amount) {
		source.addMoney(bankBook, amount);
		return true;
	}

	@Override
	public boolean delete(User user) {
		try {
			source.delete(User.class, user.getId());
			return true;
		} catch (NonexistentEntityException ex) {
			log.error(String.format("User with id %s doesn't exist", user.getId()), ex);
			return false;
		}
	}

	@Override
	public boolean update(List<Currency> currencies) {
		source.update(currencies);
		return true;
	}

	@Override
	public boolean rollback(Transaction transaction) {
		return source.rollback(transaction);
	}

	@Override
	public List<Request> getAllRequests() {
		return source.selectAll(Request.class);
	}

	@Override
	public List<Request> getForLast(long milliseconds) {
		long to = System.currentTimeMillis();
		long from = to - milliseconds;
		return source.requests(new Date(from), new Date(to));
	}
}
