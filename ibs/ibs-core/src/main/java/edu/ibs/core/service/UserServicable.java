package edu.ibs.core.service;

import edu.ibs.core.entity.Transaction.TransactionType;
import edu.ibs.core.entity.*;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public interface UserServicable extends Servicable {

	public void update(User user);

	public List<BankBook> getBankBooks(User user);

	public Transaction transfer(BankBook from, BankBook to, Money money, TransactionType type);

	public SavedPayment savePayment(Transaction transaction, User owner);

	public List<SavedPayment> getSavedPayments(User user);

	public List<Transaction> getHistory(User user, TransactionType type);

	public void delete(SavedPayment payment);
}
