package edu.ibs.core.service;

import edu.ibs.core.entity.*;
import edu.ibs.core.entity.Account.Role;
import java.util.Date;
import java.util.List;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface AdminServicable extends Servicable {

	public Account create(Role role, String email, String passwd);

	public BankBook create(User user, Money money);

	public boolean addMoney(BankBook bankBook, Money money);

	public void delete(User user);

	public void update(List<Currency> currencies);

	public void rollback(Transaction transaction);

	public List<CardRequest> getAllRequests();

	public List<CardRequest> getRequests(Date from, Date to);
}
