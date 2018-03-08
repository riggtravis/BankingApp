package com.revature.banking.login;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.BankingApplicationII.User;

public class UserLoginTest {

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
		UserLogin testUserLogin = new UserLogin();
		User testUser = new User("username", "password");
		assertTrue(testUserLogin.addLogin(testUser));
		assertEquals(testUser, testUserLogin.getUser(testUser.getUsername()));
		assertNotEquals(null, testUserLogin.getUser(testUser.getUsername()));
	}

}
