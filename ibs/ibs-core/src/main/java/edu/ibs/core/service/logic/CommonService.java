package edu.ibs.core.service.logic;

import edu.ibs.common.dto.TransactionType;
import edu.ibs.common.enums.AccountRole;
import edu.ibs.core.controller.SpecifiedJpaController;
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
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<BankBook> getBankBooks(User user) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Transaction transfer(BankBook from, BankBook to, Money money, TransactionType type) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public SavedPayment savePayment(Transaction transaction, User owner) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<SavedPayment> getSavedPayments(User user) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Transaction> getHistory(User user, TransactionType type) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void delete(SavedPayment payment) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Account login(String email, String passwd) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Currency getCurrency(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Currency> getCurrencies() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Account create(AccountRole role, String email, String passwd) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BankBook create(User user, Money money) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean addMoney(BankBook bankBook, Money money) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void delete(User user) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void update(List<Currency> currencies) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void rollback(Transaction transaction) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<CardRequest> getAllRequests() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<CardRequest> getRequests(Date from, Date to) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
