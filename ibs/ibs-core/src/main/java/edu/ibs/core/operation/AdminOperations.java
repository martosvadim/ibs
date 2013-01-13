package edu.ibs.core.operation;

import edu.ibs.common.enums.AccountRole;
import edu.ibs.common.enums.Period;
import edu.ibs.common.enums.ProviderField;
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

	public Account createAccount(AccountRole role, String email, String passwd);

	public BankBook createBankBook(User user, Money money);

	public CardBook createDebitCardBook(User user, BankBook bankBook);

	public CardBook createCreditCardBook(User user, BankBook bankBook, CreditPlan credit);

	public boolean addMoney(BankBook bankBook, Money money) throws IllegalArgumentException, FreezedException;

	public CreditPlan createCreditPlan(String name, Money limit, Period period, int periodMultiply, int percent);

	public void delete(User user);

	public void update(List<Currency> currencies);

	public void rollback(Transaction transaction) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException;

	public List<CardRequest> getAllRequests();

	public List<CardRequest> getRequests(Date from, Date to);

	public CardBook approve(CardRequest request);

	public void decline(CardRequest request, String reason);

	public User getUser(long id);

	public User getUser(String email);

	public void freeze(CreditPlan plan);

	public void unfreeze(CreditPlan plan);

	public void freeze(CardBook book);

	public void unfreeze(CardBook book);

	public void freeze(BankBook book);

	public void unfreeze(BankBook book);

	public void update(User user);

	public Provider createProvider(String company, String bookDescription, Currency currency, ProviderField... fields);

	public void deleteProvider(Provider provider);

	public User getUserByPassport(String passportNumber);

	public <T extends AbstractEntity> List<T> selectEntities(Class<T> clazz, int from, int to);
}
