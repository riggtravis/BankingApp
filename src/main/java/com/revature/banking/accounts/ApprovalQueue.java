package com.revature.banking.accounts;

import java.util.LinkedList;
import java.util.Queue;

public class ApprovalQueue {
	// Each time an account is created it needs to be pushed onto the approval Queue
	Queue<Account> pendingApprovals;
	
	public ApprovalQueue () {
		pendingApprovals = new LinkedList<Account>();
	}
	
	Account checkNextAccount () {
		return pendingApprovals.poll();
	}
	
	public void addNewAccount (Account newAccount) {
		pendingApprovals.add(newAccount);
	}
}
