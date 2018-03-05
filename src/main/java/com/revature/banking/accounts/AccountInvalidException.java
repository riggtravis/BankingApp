package com.revature.banking.accounts;

public class AccountInvalidException extends Exception {
	/**
	 * Exceptions need to be serializable
	 */
	private static final long serialVersionUID = -8242388594468557637L;
	
	public AccountInvalidException () {
		super();
	}
	
	public AccountInvalidException (String message) {
		super(message);
	}
}
