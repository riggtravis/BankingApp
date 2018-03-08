package com.revature.banking.accounts;

public class BadWithDrawalException extends Exception {
	/**
	 * Exceptions need to be serializable
	 */
	private static final long serialVersionUID = -8628413407406274775L;
	
	public BadWithDrawalException() {
		super();
	}

	public BadWithDrawalException(String message) {
		super(message);
	}
}
