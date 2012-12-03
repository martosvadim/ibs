package edu.ibs.core.service;

import edu.ibs.core.entity.Currency;
import edu.ibs.core.entity.User;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface Servicable {

	public User getUser(String email, String passwd) throws PersistenceException;

	public Currency getCurrency(String name) throws PersistenceException;

	public List<Currency> getCurrencies() throws PersistenceException;
}
