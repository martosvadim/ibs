package edu.ibs.core.service;

import edu.ibs.core.beans.BankBook;
import edu.ibs.core.beans.Request;
import edu.ibs.core.beans.Transaction;
import edu.ibs.core.beans.User;
import java.util.Currency;
import java.util.List;

/**
 *
 * @author Vadim Martos @date Oct 22, 2012
 */
public interface AdminServicable extends Servicable {

	public User createUser(User.Role role, String email, String passwd);

	public boolean createBankBook(User user, Currency currency, long balance);

	public boolean addMoney(BankBook bankBook, Currency currency, long amount);

	public boolean deleteUser(User user);

	public boolean updateCurrency(Currency currency);

	public boolean rollbackTransaction(Transaction transaction);

	public List<Request> getAllRequests();

	public List<Request> getForLast(long milliseconds);

	public boolean process(Request request, boolean submit);
}
