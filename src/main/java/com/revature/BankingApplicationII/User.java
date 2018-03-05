package com.revature.BankingApplicationII;

import java.io.Serializable;
import java.util.HashMap;

import com.revature.banking.accounts.Account;
import com.revature.banking.login.BadPasswordException;

public class User implements Serializable {
	/**
	 * In order to save the hashmap of users, we need to be able to serialize Users
	 */
	private static final long serialVersionUID = 6433681909202567390L;

	// Users have user names
	private String username;
	
	// Users have passwords
	private String password;
	
	// Users have accounts
	private HashMap<String, Account> accounts;
	
	public User (String newUsername, String newPassword) {
		this.username = newUsername;
		this.password = newPassword;
		accounts = new HashMap<String, Account>();
	}

	public String getUsername () {
		return username;
	}

	public boolean checkPassword (String triedPassword) {
		return (triedPassword.equals(this.password));
	}

	public void changePassword (String newPassword, String oldPassword) throws BadPasswordException {
		if (this.checkPassword(oldPassword)) {
			this.password = newPassword;
		} else {
			throw new BadPasswordException("Could not set new password. Old password didn't match");
		}
	}
	
	public Account getAccount (String accountType) {
		return accounts.get(accountType);
	}
	
	public void applyForAccount () {
		accounts.put("checking", new Account());
	}
	
	public void applyForAccount (String accountType) {
		accounts.put(accountType, new Account());
	}
	
	public void applyForAccount (double initialDeposit) {
		accounts.put("checking", new Account(initialDeposit));
	}
	
	public void applyForAccount (String accountType, double initialDeposit) {
		accounts.put(accountType, new Account(initialDeposit));
	}
	
	// Users need to be able to create joint accounts
	public void applyForJointAccount (User partner) {}
	public void applyForJointAccount (User partner, String accountType) {}
	public void applyForJointAccount (User partner, double initialDeposit) {}
	public void applyForJointAccoint (User partner, String accountType, double initialDeposit) {}
}
