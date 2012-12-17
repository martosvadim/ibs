package edu.ibs.common.dto;

import edu.ibs.common.enums.CardBookType;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:25
 */
public class CardRequestDTO implements IBaseDTO {
	private long id;
	private UserDTO user;
	private BankBookDTO bankBook;
	private CardBookType type;
	private long dateCreated;
	private long dateWatched;
	private boolean approved;
	private String reason;
	private CreditPlanDTO plan;
	private CardBookDTO cardBook;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public BankBookDTO getBankBook() {
		return bankBook;
	}

	public void setBankBook(BankBookDTO bankBook) {
		this.bankBook = bankBook;
	}

	public CardBookType getType() {
		return type;
	}

	public void setType(CardBookType type) {
		this.type = type;
	}

	public long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getDateWatched() {
		return dateWatched;
	}

	public void setDateWatched(long dateWatched) {
		this.dateWatched = dateWatched;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public CreditPlanDTO getPlan() {
		return plan;
	}

	public void setPlan(CreditPlanDTO plan) {
		this.plan = plan;
	}

	public CardBookDTO getCardBook() {
		return cardBook;
	}

	public void setCardBook(CardBookDTO cardBook) {
		this.cardBook = cardBook;
	}
}
