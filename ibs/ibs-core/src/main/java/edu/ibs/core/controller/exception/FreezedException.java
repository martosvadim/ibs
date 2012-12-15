package edu.ibs.core.controller.exception;

/**
 *
 * @date Dec 15, 2012
 *
 * @author Vadim Martos
 */
public class FreezedException extends BankDataException {

	public FreezedException() {
	}

	public FreezedException(String message) {
		super(message);
	}

	public FreezedException(String message, Throwable cause) {
		super(message, cause);
	}

	public FreezedException(Throwable cause) {
		super(cause);
	}
}
