package edu.ibs.core.service;

import edu.ibs.core.entity.*;
import java.util.Date;
import java.util.List;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface AdminServicable extends Servicable {

	public User create(User.Role role, String email, String passwd);

	public BankBook create(User user, Money money);

	public boolean addMoney(BankBook bankBook, Money money);

	public boolean delete(User user);

	public boolean update(List<Currency> currencies);

	public boolean rollback(Transaction transaction);

	public List<Request> getAllRequests();

	public List<Request> getRequests(Date from, Date to);
}
