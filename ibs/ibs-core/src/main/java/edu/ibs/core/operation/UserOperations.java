package edu.ibs.core.operation;

import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.enums.CardBookType;
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
public interface UserOperations extends CommonOperations {

    public void update(Account acc);

    public List<BankBook> getBankBooks(User user);

    public List<CardBook> getCardBooks(User user);

    public List<CardBook> getCardBooks(User user, BankBook book, CardBookType type);

    public List<CardBook> getCardBooks(User user, BankBook book);

    public List<CardBook> getCardBooks(User user, CardBookType type);

    public List<CardBook> getCardBooks(BankBook bankBook);

    public List<CardBook> getCardBooks(BankBook bankBook, CardBookType type);

    public void update(CardBook cardBook);

    public void reassign(CardBook cardBook, String toUserWithEmail);

    public boolean checkIfReassignmentIsAvailable(String toUserWithEmail);

    public Transaction pay(CardBook from, long toCardBookID, Money money, TransactionType type) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException;

    public Transaction pay(SavedPayment savedPayment, Money money) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException;

    public SavedPayment savePayment(Transaction transaction, User owner);

    public List<SavedPayment> getSavedPayments(User user);

    public List<Transaction> getAllHistory(User user, TransactionType type);

    public List<Transaction> getAllHistory(User user, TransactionType type, Date from, Date to);

    public List<Transaction> getHistoryIncome(User user, TransactionType type);

    public List<Transaction> getHistoryIncome(User user, TransactionType type, Date from, Date to);

    public List<Transaction> getHistoryOutcome(User user, TransactionType type);

    public List<Transaction> getHistoryOutcome(User user, TransactionType type, Date from, Date to);

    public void delete(SavedPayment payment);

    public CardRequest requestDebitCard(User user, BankBook bankBook);

    public CardRequest requestCreditCard(User user, BankBook bankBook, CreditPlan plan);

    public List<CardRequest> getCardRequestsOf(User user, boolean watched);

    public Autopay createAutopay(CardBook from, CardBook to, Money money, long period);

    public Autopay createAutopay(Transaction tr, Money money, long period);

    public Autopay createAutopay(Transaction tr, long period);

    public Autopay createAutopay(SavedPayment payment, Money money, long period);

    public Autopay createAutopay(SavedPayment payment, long period);

    public List<Autopay> getAutopaysOf(User owner);
}
