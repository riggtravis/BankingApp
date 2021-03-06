package com.revature.banking.accounts;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class ApprovalQueue implements Serializable {
	/**
	 * We need to be able to save ApprovalQueues
	 */
	private static final long serialVersionUID = -1662782095083447726L;
	// Each time an account is created it needs to be pushed onto the approval Queue
	Queue<Account> pendingApprovals;
	
	public ApprovalQueue () {
		pendingApprovals = new LinkedList<Account>();
	}
	
	public Account checkNextAccount () {
		return pendingApprovals.poll();
	}
	
	public void addNewAccount (Account newAccount) {
		pendingApprovals.add(newAccount);
	}
}
