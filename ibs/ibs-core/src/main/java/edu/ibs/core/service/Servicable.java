package edu.ibs.core.service;

import edu.ibs.core.entity.Account;
import edu.ibs.core.entity.Currency;
import java.util.List;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface Servicable {

	public Account login(String email, String passwd);

	public Currency getCurrency(String name);

	public List<Currency> getCurrencies();
}
