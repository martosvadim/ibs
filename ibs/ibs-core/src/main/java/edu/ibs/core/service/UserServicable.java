package edu.ibs.core.service;

import edu.ibs.common.dto.TransactionType;
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
public interface UserServicable extends Servicable {

	public void update(User user);

	public List<BankBook> getBankBooks(User user);

	public Transaction transfer(CardBook from, CardBook to, Money money, TransactionType type) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException;

	public SavedPayment savePayment(Transaction transaction, User owner);

	public List<SavedPayment> getSavedPayments(User user);

	public List<Transaction> getHistory(User user, TransactionType type);

	public List<Transaction> getHistory(User user, TransactionType type, Date from, Date to);

	public List<Transaction> getHistoryIncome(User user, TransactionType type);

	public List<Transaction> getHistoryIncome(User user, TransactionType type, Date from, Date to);

	public List<Transaction> getHistoryOutcome(User user, TransactionType type);

	public List<Transaction> getHistoryOutcome(User user, TransactionType type, Date from, Date to);

	public void delete(SavedPayment payment);
}
