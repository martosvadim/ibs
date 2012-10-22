package edu.ibs.core.beans;

/**
 *
 * @author Vadim Martos
 */
public class BankBook {

	private final long id;
	private final User owner;
	private final Currency currency;
	private long balance;
	private boolean freezed;

	public BankBook(long id, User owner, Currency currency, long balance, boolean freezed) {
		this.id = id;
		this.owner = owner;
		this.currency = currency;
		this.balance = balance;
		this.freezed = freezed;
	}
}
