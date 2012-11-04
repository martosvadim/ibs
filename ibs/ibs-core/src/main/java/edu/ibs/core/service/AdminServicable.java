package edu.ibs.core.service;

import edu.ibs.core.entity.*;
import java.util.List;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface AdminServicable extends Servicable {

	public User create(User.Role role, String email, String passwd);

	public BankBook create(User user, Currency currency, long balance);

	public boolean addMoney(BankBook bankBook, long amount);

	public boolean delete(User user);

	public boolean update(List<Currency> currencies);

	public boolean rollback(Transaction transaction);

	public List<Request> getAllRequests();

	public List<Request> getForLast(long milliseconds);
}
