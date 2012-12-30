package edu.ibs.common.dto;

import edu.ibs.common.enums.CardBookType;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:19
 */
public class CardBookDTO implements IBaseDTO {
	private long id;
	private CardBookType type;
	private long dateExpire;
	private boolean freezed;
	private int pin;
	private BankBookDTO bankBook;
	private CreditDTO credit;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CardBookType getType() {
		return type;
	}

	public void setType(CardBookType type) {
		this.type = type;
	}

	public long getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(long dateExpire) {
		this.dateExpire = dateExpire;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public BankBookDTO getBankBook() {
		return bankBook;
	}

	public void setBankBook(BankBookDTO bankBook) {
		this.bankBook = bankBook;
	}

	public CreditDTO getCredit() {
		return credit;
	}

	public void setCredit(CreditDTO credit) {
		this.credit = credit;
	}
}
