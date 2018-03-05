package com.revature.banking.login;

public class BadPasswordException extends Exception {
	/**
	 * Exceptions need to be serializable
	 */
	private static final long serialVersionUID = -2257872876438830998L;

	public BadPasswordException() {
		super();
	}

	public BadPasswordException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}
