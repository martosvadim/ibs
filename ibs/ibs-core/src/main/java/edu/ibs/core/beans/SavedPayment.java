package edu.ibs.core.beans;

/**
 *
 * @author Vadim Martos
 */
public class SavedPayment {

	private final long id;
	private final User user;
	private final Transaction transaction;

	public SavedPayment(long id, User user, Transaction transaction) {
		this.id = id;
		this.user = user;
		this.transaction = transaction;
	}
}
