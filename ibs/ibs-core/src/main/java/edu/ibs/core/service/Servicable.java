package edu.ibs.core.service;

import edu.ibs.core.entity.Currency;
import edu.ibs.core.entity.User;
import java.util.List;

/**
 *
 * @author Vadim Martos @date Oct 22, 2012
 */
public interface Servicable {

	public User getUser(String email, String passwd);

	public Currency getCurrency(String name);

	public List<Currency> getCurrencies();
}
