package edu.ibs.core.service;

import edu.ibs.core.entity.*;
import java.util.Date;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface AdminServicable extends Servicable {

	public User create(User.Role role, String email, String passwd) throws PersistenceException, IllegalArgumentException;

	public BankBook create(User user, Money money) throws IllegalArgumentException;

	public boolean addMoney(BankBook bankBook, Money money) throws IllegalArgumentException;

	public void delete(User user) throws PersistenceException;

	public void update(List<Currency> currencies) throws PersistenceException;

	public void rollback(Transaction transaction) throws IllegalArgumentException;

	public List<Request> getAllRequests();

	public List<Request> getRequests(Date from, Date to);
}
