/**
 * 
 */
package com.revature.BankingApplicationII;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.banking.accounts.Teller;
import com.revature.banking.login.BadPasswordException;

/**
 * @author Travis Rigg
 *
 */
public class UserTest {
	private User testUser;
	private User accountPartner;
	private Teller testTeller;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testUser = new User("testName", "testPass");
		accountPartner = new User("partnerName", "partnerPass");
		testTeller = new Teller();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetName() {
		assertEquals("testName", testUser.getUsername());
	}
	
	@Test
	public void testCheckPassword() {
		assertTrue(testUser.checkPassword("testPass"));
		assertFalse(testUser.checkPassword("wrongPassword"));
	}
	
	@Test
	public void testChangePassword() {
		try {
			testUser.changePassword("newPass", "testPass");
		} catch (BadPasswordException e) {
			fail("Change Password test failed");
		}
		assertTrue(testUser.checkPassword("newPass"));
		assertFalse(testUser.checkPassword("testPass"));
	}
	
	@Test(expected = BadPasswordException.class)
	public void testBadPasswordChange() throws BadPasswordException {
		testUser.changePassword("newPass", "wrongPass");
		thrown.expect(BadPasswordException.class);
	}
	
	@Test
	public void testAccountAccessors() {
		Double testBalance = 0.0;
		Double bigMoney	= 100.0;

		// Make sure test user has an account
		testUser.applyForAccount();
		assertEquals(testBalance, new Double(testUser.getAccount("checking").getBalance()));
		testUser.applyForAccount("savings");
		assertEquals(testBalance, new Double(testUser.getAccount("savings").getBalance()));

		testUser.applyForAccount("CD", bigMoney);
		testTeller.approveAccount(testUser.getAccount("CD"));
		assertEquals(bigMoney, new Double(testUser.getAccount("CD").getBalance()));
	}
	
	@Test
	public void testAccountAccessorWithOnlyMoney() {
		testUser.applyForAccount(100.0);
		testTeller.approveAccount(testUser.getAccount("checking"));
		assertEquals(new Double(100.0), new Double(testUser.getAccount("checking").getBalance()));
	}
	
	@Test
	public void testJointAccounts() {
		testUser.applyForJointAccount(accountPartner);
		assertEquals(testUser.getAccount("checking"), accountPartner.getAccount("checking"));
		testUser.applyForJointAccount(accountPartner, "savings");
		assertEquals(testUser.getAccount("savings"), accountPartner.getAccount("savings"));
		testUser.applyForJointAccoint(accountPartner, "CD", 0.0);
		assertEquals(testUser.getAccount("CD"), accountPartner.getAccount("CD"));
	}
	
	@Test
	public void testJointAccountsWithOnlyMoney () {
		testUser.applyForJointAccount(accountPartner, 0.0);
		assertEquals(testUser.getAccount("checking"), accountPartner.getAccount("checking"));
	}
}
