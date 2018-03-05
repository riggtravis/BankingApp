package com.revature.banking.accounts;

import java.io.Serializable;

// The pending accounts need to be serialized
public class Account implements Serializable {
	/**
	 * In order to save the pendingAccounts for later, we need to make account a serializable class
	 */
	private static final long serialVersionUID = -7494809255262434694L;

	// Accounts have balances
	private double balance;
	
	// Accounts need to be approved by bank employees
	// 0 represents an account that hasn't been approved
	// A negative number represents a denied account
	// A positive number represents an approved account
	private short approved;
	
	// Accounts need to have a type
	private String accountType;
	
	// There needs to be a Queue for approvals
	static ApprovalQueue pendingAccounts = new ApprovalQueue();
	
	public Account () {
		this(0.0);
	}

	public Account (double firstDeposit) {
		this("checking", firstDeposit);
	}
	
	public Account (String newType) {
		this(newType, 0.0);
	}
	
	public Account (String newType, double firstDeposit) {
		this.balance = firstDeposit;
		this.accountType = newType;
		this.approved = 0;
		pendingAccounts.addNewAccount(this);
	}
	
	public double getBalance() {
		if (approved > 0) {
			return balance;
		}
		return 0.0;
	}
	
	public void makeDeposit(double deposit) throws AccountInvalidException {
		if (approved <= 0) {
			throw new AccountInvalidException("Attempted deposit to unapproved account");
		}
		this.balance += deposit;
	}
	
	public void makeWithdrawal(double withdrawal) throws AccountInvalidException, BadWithDrawalException {
		if (approved <= 0) {
			throw new AccountInvalidException("Attempted withdrawal from unapproved account");
		} else {
			if (0 < withdrawal && withdrawal < balance) {
				this.balance -= withdrawal;
			} else {
				throw new BadWithDrawalException("You can't make that withdrawal");
			}
		}
	}
	
	public void transfer(Account toAccount, double amount) throws AccountInvalidException, BadWithDrawalException {
		this.makeWithdrawal(amount);
		toAccount.makeDeposit(amount);
	}
	
	void approveAccount() {
		this.approved = 1;
	}
	
	void rejectAccount() {
		this.approved = -1;
	}
	
	public short checkApproval() {
		return this.approved;
	}

	public String getAccountType() {
		return accountType;
	}
}
