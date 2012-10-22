package edu.ibs.core.service;

import edu.ibs.core.beans.BankBook;
import edu.ibs.core.beans.SavedPayment;
import edu.ibs.core.beans.Transaction;
import edu.ibs.core.beans.User;
import java.util.Currency;
import java.util.List;

/**
 *
 * @author Vadim Martos @date Oct 22, 2012
 */
public interface UserServicable extends Servicable {

	public boolean updateUser(User user);

	public List<BankBook> getBankBooks(User user);

	public Transaction transfer(User user, BankBook from, BankBook to, Transaction.Type type, Currency currency, long amount);

	public SavedPayment savePayment(Transaction transaction);

	public List<SavedPayment> getSavedPayments(User user);

	public List<Transaction> getHistory(User user, Transaction.Type type);

	public boolean deleteSavedPayment(SavedPayment payment);
}
