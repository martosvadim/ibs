package edu.ibs.common.dto;

/**
 * User: EgoshinME
 * Date: 17.12.12
 * Time: 5:27
 */
public class SavedPaymentDTO implements IBaseDTO {
	private long id;
	private TransactionDTO transaction;
	private UserDTO user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TransactionDTO getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionDTO transaction) {
		this.transaction = transaction;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
}
