package com.revature.banking.accounts;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ApprovalQueueTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		ApprovalQueue testQueue = new ApprovalQueue();
		Account testAccount = new Account();
		testQueue.addNewAccount(testAccount);
		assertEquals(testAccount, testQueue.checkNextAccount());
	}
}
