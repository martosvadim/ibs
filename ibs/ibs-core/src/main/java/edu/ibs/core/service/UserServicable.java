package edu.ibs.core.service;

import edu.ibs.core.entity.BankBook;
import edu.ibs.core.entity.SavedPayment;
import edu.ibs.core.entity.Transaction;
import edu.ibs.core.entity.Transaction.TransactionType;
import edu.ibs.core.entity.User;
import java.util.List;

/**
 *
 * @author Vadim Martos @date Oct 22, 2012
 */
public interface UserServicable extends Servicable {

	public boolean updateUser(User user);

	public List<BankBook> getBankBooks(User user);

	public Transaction transfer(BankBook from, BankBook to, TransactionType type, long amount);

	public SavedPayment savePayment(Transaction transaction, User owner);

	public List<SavedPayment> getSavedPayments(User user);

	public List<Transaction> getHistory(User user, TransactionType type);

	public boolean deleteSavedPayment(SavedPayment payment);
}
