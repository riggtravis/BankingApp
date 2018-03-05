package com.revature.banking.accounts;

import static org.junit.Assert.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.BankingApplicationII.UserInterface;

public class AccountTest {
	private Account testAccount;
	Teller testTeller;

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass () throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass () throws Exception {
	}

	@Before
	public void setUp () throws Exception {
		testAccount = new Account();
		testTeller = new Teller();
	}

	@After
	public void tearDown () throws Exception {
	}

	@Test
	public void testGetBalance () {
		assertEquals(new Double(0.0), new Double(testAccount.getBalance()));
		
		testAccount = new Account("savings");
		assertEquals(new Double(0.0), new Double(testAccount.getBalance()));
		
		testAccount = new Account(100.0);
		testTeller.approveAccount(testAccount);

		assertEquals(new Double(100.0), new Double(testAccount.getBalance()));
		
		testAccount = new Account("savings", 100.0);
		testTeller.approveAccount(testAccount);
		assertEquals(new Double(100.0), new Double(testAccount.getBalance()));
	}
	
	@Test(expected = AccountInvalidException.class)
	public void testUnapprovedAccountDeposit () throws AccountInvalidException {
		testAccount.makeDeposit(0.01);
		thrown.expect(AccountInvalidException.class);
	}
	
	@Test
	public void testApprovedAccountDeposit () {
		testTeller.approveAccount(testAccount);
		try {
			testAccount.makeDeposit(0.01);
		} catch (AccountInvalidException e) {
			// TODO Auto-generated catch block
			fail("Accounts aren't being approved");
		}
		assertEquals(new Double(0.01), new Double(testAccount.getBalance()));
	}
	
	@Test(expected = AccountInvalidException.class)
	public void testUnapprovedAccountWithdrawal () throws AccountInvalidException {
		testAccount = new Account(100.0);
		try {
			testAccount.makeWithdrawal(0.01);
		} catch (BadWithDrawalException e) {
			fail("A funds exception occured when an account exception should have occured");
		}
		thrown.expect(AccountInvalidException.class);
	}
	
	@Test(expected = BadWithDrawalException.class)
	public void testInsufficientFundsWithdrawl () throws BadWithDrawalException {
		testAccount = new Account(0.0);
		testTeller.approveAccount(testAccount);
		try {
			testAccount.makeWithdrawal(0.01);
		} catch (AccountInvalidException e) {
			fail("An account exception occured when a funds exception should have occured");
		}
		thrown.expect(BadWithDrawalException.class);
	}
	
	@Test(expected = BadWithDrawalException.class)
	public void testNegativeWithdrawal () throws BadWithDrawalException {
		testAccount = new Account(100.0);
		testTeller.approveAccount(testAccount);
		try {
			testAccount.makeWithdrawal(-0.01);
		} catch (AccountInvalidException e) {
			fail("Cannot approve negative withdrawal account");
		}
		thrown.expect(BadWithDrawalException.class);
	}
	
	@Test
	public void testApprovedAccountWithdrawawl () {
		final Logger logger = LogManager.getLogger(UserInterface.class);
		testAccount = new Account(100.0);
		testTeller.approveAccount(testAccount);
		logger.debug("Testing account withdrawal on account with approval status " + testAccount.checkApproval());
		try {
			testAccount.makeWithdrawal(0.01);
		} catch (AccountInvalidException e) {
			fail("Accounts aren't being approved");
		} catch (BadWithDrawalException e) {
			fail("This account has the wrong amount of funds");
		}
		assertEquals(new Double(99.99), new Double(testAccount.getBalance()));
	}
	
	@Test
	public void testTransfer () {
		Account secondAccount = new Account(100.0);
		testTeller.approveAccount(testAccount);
		testTeller.approveAccount(secondAccount);
		try {
			secondAccount.transfer(testAccount, 50.0);
		} catch (AccountInvalidException e) {
			fail("One or more accounts isn't approved");
		} catch (BadWithDrawalException e) {
			fail("The withdrawing account doesn't have enough funds");
		}
		assertEquals(new Double(testAccount.getBalance()), new Double(secondAccount.getBalance()));
	}
	
	@Test
	public void testAproval () {
		testAccount.approveAccount();
		assertEquals(1, testAccount.checkApproval());
	}
	
	@Test
	public void testRejection () {
		testAccount.rejectAccount();
		assertEquals(-1, testAccount.checkApproval());
	}
	
	@Test
	public void testCheckStatus () {
		assertEquals(0, testAccount.checkApproval());
	}
	
	@Test
	public void checkGetAccountType () {
		assertEquals("checking", testAccount.getAccountType());
	}
}
