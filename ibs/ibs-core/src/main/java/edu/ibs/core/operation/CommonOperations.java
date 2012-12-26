package edu.ibs.core.operation;

import edu.ibs.core.entity.Account;
import edu.ibs.core.entity.CreditPlan;
import edu.ibs.core.entity.Currency;
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

	public Currency getCurrency(String name);

	public List<Currency> getCurrencies();

	public List<CreditPlan> getCreditPlans();

	public CreditPlan getCreditPlan(String name);

	public boolean bankBookExists(long id);

	public boolean cardBookExists(long id);
}
