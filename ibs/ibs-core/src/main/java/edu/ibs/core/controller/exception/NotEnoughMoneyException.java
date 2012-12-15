package edu.ibs.core.controller.exception;

/**
 *
 * @date Dec 15, 2012
 *
 * @author Vadim Martos
 */
public class NotEnoughMoneyException extends BankDataException {

	public NotEnoughMoneyException() {
	}

	public NotEnoughMoneyException(String message) {
		super(message);
	}

	public NotEnoughMoneyException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughMoneyException(Throwable cause) {
		super(cause);
	}
}
