package com.revature.BankingApplicationII;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import static org.junit.Assert.*;

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
		assertEquals(testUser, UserInterface.getCurrentUser(s));
	}
	
	@Test
	public void testSerializedLogIn() {
		s = new Scanner("1 testUser testPassword");
		
		// Serialize a loginMap to test against
		commitSerialData();
		assertEquals(testUser, UserInterface.getCurrentUser(s));
	}
	
	@Test
	public void testNewUser() {
		s = new Scanner("2 secondUser secondPassword");
		
		UserLogin checkLogin = commitSerialData();
		assertEquals(secondUser, UserInterface.getCurrentUser(s));
		
		// Also make sure that the new user was committed to the map
		assertEquals(secondUser, checkLogin.getUser("secondUser"));
	}
	
	@Test 
	public void testNewUserRetry() {
		s = new Scanner("2 testUser testPassword 2 secondUser secondPassword");
		
		commitSerialData();
		assertEquals(secondUser, UserInterface.getCurrentUser(s));
	}
	
	@Test
	public void testNoUser() {
		s = new Scanner("3");
		assertEquals(null, UserInterface.getCurrentUser(s));
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
