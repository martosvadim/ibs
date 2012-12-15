package edu.ibs.core.controller.exception;

/**
 *
 * @date Dec 15, 2012
 *
 * @author Vadim Martos
 */
public class BankDataException extends Exception {

	public BankDataException(Throwable cause) {
		super(cause);
	}

	public BankDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public BankDataException(String message) {
		super(message);
	}

	public BankDataException() {
	}
}
