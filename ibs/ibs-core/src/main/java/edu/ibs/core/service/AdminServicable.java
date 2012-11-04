package edu.ibs.core.service;

import edu.ibs.core.entity.*;
import java.util.List;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface AdminServicable extends Servicable {

	public User createUser(User.Role role, String email, String passwd);

	public BankBook createBankBook(User user, Currency currency, long balance);

	public boolean addMoney(BankBook bankBook, long amount);

	public boolean deleteUser(User user);

	public boolean updateCurrency(Currency currency);

	public boolean rollbackTransaction(Transaction transaction);

	public List<Request> getAllRequests();

	public List<Request> getForLast(long milliseconds);
}
