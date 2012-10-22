package edu.ibs.core.service;

import edu.ibs.core.beans.Transaction.Type;
import edu.ibs.core.beans.*;
import edu.ibs.core.data.UserDAO;
import java.util.Currency;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Vadim Martos @date Oct 22, 2012
 */
final class CommonService implements UserServicable, AdminServicable {

	public static final String VALID_EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEXP);
	private UserDAO userDAO = null;

	public User createUser(User.Role role, String email, String passwd) {
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException(String.format("Invalid email: '%s'", email));
		} else {
			User user = userDAO.create(email, role);
			return user;
		}
	}

	public boolean updateUser(User user) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<BankBook> getBankBooks(User user) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Transaction transfer(User user, BankBook from, BankBook to, Type type, Currency currency, long amount) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public SavedPayment savePayment(Transaction transaction) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<SavedPayment> getSavedPayments(User user) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Transaction> getHistory(User user, Type type) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean deleteSavedPayment(SavedPayment payment) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public User getUser(String email, String passwd) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Currency getCurrency(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Currency> getCurrencies() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean createBankBook(User user, Currency currency, long balance) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean addMoney(BankBook bankBook, Currency currency, long amount) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean deleteUser(User user) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean updateCurrency(Currency currency) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean rollbackTransaction(Transaction transaction) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Request> getAllRequests() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Request> getForLast(long milliseconds) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean process(Request request, boolean submit) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
