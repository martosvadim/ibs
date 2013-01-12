package edu.ibs.core.operation;

import edu.ibs.core.entity.*;
import java.util.List;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface CommonOperations {

	public Account login(String email, String passwd);

	public Account register(String email, String passwd);

	public boolean isFree(String email);

	public boolean isValid(String email);

	public Currency getCurrency(String name);

	public List<Currency> getCurrencies();

	public List<CreditPlan> getCreditPlans();

	public List<CreditPlan> getCreditPlansFor(Currency curr);

	public CreditPlan getCreditPlan(String name);

	public boolean bankBookExists(long id);

	public BankBook getBankBook(Account user, long id);

	public boolean cardBookExists(long id);
	
	public List<Provider> getProviderList();
}
