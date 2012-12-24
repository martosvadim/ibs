package edu.ibs.core.service.logic;

import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.core.controller.SpecifiedJpaController;
import edu.ibs.core.controller.exception.FreezedException;
import edu.ibs.core.controller.exception.NotEnoughMoneyException;
import edu.ibs.core.entity.*;
import edu.ibs.core.service.AdminServicable;
import edu.ibs.core.service.UserServicable;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @date Oct 22, 2012
 *
 * @author Vadim Martos
 */
public final class CommonService implements UserServicable, AdminServicable {

	public static final String VALID_EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEXP);
	private final SpecifiedJpaController source = SpecifiedJpaController.instance();
	private final Logger log = Logger.getLogger(CommonService.class);

	@Override
	public void update(User user) {
		source.update(user);
	}

	@Override
	public List<BankBook> getBankBooks(User user) {
		return source.bankBooks(user);
	}

	@Override
	public Transaction transfer(CardBook from, CardBook to, Money money, TransactionType type) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException {
		return source.pay(from, to, money, type);
	}

	@Override
	public void rollback(Transaction transaction) throws IllegalArgumentException, FreezedException, NotEnoughMoneyException {
		source.rollback(transaction);
	}

	@Override
	public SavedPayment savePayment(Transaction transaction, User owner) {
		SavedPayment sp = new SavedPayment(transaction, owner);
		source.insert(sp);
		return sp;
	}

	@Override
	public List<SavedPayment> getSavedPayments(User user) {
		return source.savedPayments(user);
	}

	@Override
	public void delete(SavedPayment payment) {
		source.delete(payment.getClass(), payment.getId());
	}

	@Override
	public Account login(String email, String passwd) {
		return source.getUserAccount(email, passwd);
	}

	@Override
	public Currency getCurrency(String name) {
		return source.currency(name);
	}

	@Override
	public List<Currency> getCurrencies() {
		return source.selectAll(Currency.class);
	}

	@Override
	public Account create(AccountRole role, String email, String passwd) {
		Account acc = new Account(email, passwd, role);
		source.insert(acc);
		return acc;
	}

	@Override
	public BankBook create(User user, Money money) {
		BankBook bb = new BankBook(user, money);
		source.insert(bb);
		return bb;
	}

	@Override
	public boolean addMoney(BankBook bankBook, Money money) {
		source.addMoney(bankBook, money);
		return true;
	}

	@Override
	public void delete(User user) {
		source.delete(user.getClass(), user.getId());
	}

	@Override
	public void update(List<Currency> currencies) {
		source.batchUpdate(currencies);
	}

	@Override
	public List<CardRequest> getAllRequests() {
		return source.requests(null, null);
	}

	@Override
	public List<CardRequest> getRequests(Date from, Date to) {
		return source.requests(from, to);
	}

	@Override
	public List<Transaction> getHistory(User user, TransactionType type) {
		return source.historyAll(user, type, null, null);
	}

	@Override
	public List<Transaction> getHistory(User user, TransactionType type, Date from, Date to) {
		return source.historyAll(user, type, from, to);
	}

	@Override
	public List<Transaction> getHistoryIncome(User user, TransactionType type) {
		return source.historyIncome(user, type, null, null);
	}

	@Override
	public List<Transaction> getHistoryIncome(User user, TransactionType type, Date from, Date to) {
		return source.historyIncome(user, type, from, to);
	}

	@Override
	public List<Transaction> getHistoryOutcome(User user, TransactionType type) {
		return source.historyOutcome(user, type, null, null);
	}

	@Override
	public List<Transaction> getHistoryOutcome(User user, TransactionType type, Date from, Date to) {
		return source.historyOutcome(user, type, from, to);
	}
}
