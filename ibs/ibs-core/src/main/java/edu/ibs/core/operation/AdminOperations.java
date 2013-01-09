package edu.ibs.core.operation;

import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.enums.Period;
import edu.ibs.core.controller.exception.FreezedException;
import edu.ibs.core.controller.exception.NotEnoughMoneyException;
import edu.ibs.core.entity.*;

import java.util.Date;
import java.util.List;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface AdminOperations extends CommonOperations {

	public Account create(AccountRole role, String email, String passwd);

	public BankBook create(User user, Money money);

	public CardBook create(User user, BankBook bankBook);

	public CardBook create(User user, BankBook bankBook, CreditPlan credit);

	public boolean addMoney(BankBook bankBook, Money money) throws IllegalArgumentException, FreezedException;

	public CreditPlan create(String name, Money limit, Period period, int periodMultiply, int percent);

	public void delete(User user);

	public void update(List<Currency> currencies);

	public void rollback(Transaction transaction) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException;

	public List<CardRequest> getAllRequests();

	public List<CardRequest> getRequests(Date from, Date to);

	public CardBook approve(CardRequest request);

	public void decline(CardRequest request, String reason);
    
    public User getUser(long id);
}
