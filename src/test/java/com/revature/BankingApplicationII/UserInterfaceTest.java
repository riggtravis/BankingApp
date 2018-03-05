package com.revature.BankingApplicationII;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import static org.junit.Assert.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.BankingApplicationII.User;
import com.revature.BankingApplicationII.UserInterface;
import com.revature.banking.login.UserLogin;

public class UserInterfaceTest {
	private Scanner s;
	private User testUser = new User("testUser", "testPassword");
	private User secondUser = new User("secondUser", "secondPassword");

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
		// Delete any serialized data from last test
		File deleteFile = new File("loginMap.ser");
		deleteFile.delete();
	}

	@Test
	public void testNoSerializedUserMap() {
		s = new Scanner("2 testUser testPassword");
		UserLogin testMap = new UserLogin();
		assertEquals(testUser.getUsername(), UserInterface.getCurrentUser(s, testMap).getUsername());
	}
	
	@Test
	public void testSerializedLogIn() {
		s = new Scanner("1 testUser testPassword");
		
		// Serialize a loginMap to test against
		UserLogin testMap = commitSerialData();
		assertEquals(testUser.getUsername(), UserInterface.getCurrentUser(s, testMap).getUsername());
	}
	
	@Test
	public void testNewUser() {
		s = new Scanner("2 secondUser secondPassword");
		
		UserLogin checkLogin = commitSerialData();
		final Logger logger = LogManager.getLogger(UserInterface.class);
		
		logger.debug("Checking that the user interface gave the right User");
		User testNewUserUser = UserInterface.getCurrentUser(s, checkLogin);
		assertEquals(secondUser.getUsername(), testNewUserUser.getUsername());
		
		// Also make sure that the new user was committed to the map
		logger.debug("Checking that the user interface committed the user to the HashMap");
		assertEquals(testNewUserUser, checkLogin.getUser("secondUser"));
	}
	
	@Test 
	public void testNewUserRetry() {
		s = new Scanner("2 testUser testPassword 2 secondUser secondPassword");
		
		UserLogin testMap = commitSerialData();
		assertEquals(secondUser.getUsername(), UserInterface.getCurrentUser(s, testMap).getUsername());
	}
	
	@Test
	public void testNoUser() {
		s = new Scanner("3");
		UserLogin testMap = new UserLogin();
		assertEquals(null, UserInterface.getCurrentUser(s, testMap));
	}
	
	private UserLogin commitSerialData() {
		UserLogin testMap = new UserLogin();
		testMap.addLogin(testUser);
		try (FileOutputStream testFile = new FileOutputStream("loginMap.ser");) {
			ObjectOutputStream out = new ObjectOutputStream(testFile);
			out.writeObject(testMap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return testMap;
	}
}
