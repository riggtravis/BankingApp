package com.revature.banking.accounts;

public class Teller {
	public void approveAccount (Account pendingAccount) {
		pendingAccount.approveAccount();
	}
	
	public void rejectAcount (Account pendingAccount) {
		pendingAccount.rejectAccount();
	}
}
