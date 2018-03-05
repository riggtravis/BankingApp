package com.revature.BankingApplicationII;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.revature.banking.login.UserLogin;

public class UserInterface {
	private static final Logger logger = LogManager.getLogger(UserInterface.class);

	public static void main (String[] args) {
		Scanner s;
		s = new Scanner(System.in);
		// Greet the user with the chance to log in or create an account
		User currentUser = getCurrentUser(s);
		
		// If no current user was fetched we don't want to do any of this
		// Offer the user a menu
		if(!currentUser.equals(null)) {
			menu(currentUser);
		}
		
		System.out.println("Thank you for visiting Revbank! Revbank! It's the bankiest!");
		System.out.println("If you did not mean to leave Revbank, then we're kicking you out for being a bad person");
	}
	
	static User getCurrentUser(Scanner s) {
		// We will need to check the users map for user logins
		UserLogin loginMap;
		User returnUser = null;
		
		// We will need to store a user's input for later
		String input;
		String inputUsername;
		String inputPassword;
		
		// Load the loginMap from a file
		try (FileInputStream file = new FileInputStream("loginMap.ser");
				ObjectInputStream loginMapStream = new ObjectInputStream(file)) {
			loginMap = (UserLogin) loginMapStream.readObject();
		} catch (FileNotFoundException e) {
			// This is actually okay. It just means we need to create a new loginMap
			loginMap = new UserLogin();
		} catch (IOException e) {
			// This is less recoverable. It means that the operating system letting us have files
			System.out.println("Sorry, the bank is not currently open. Please leave");
			logger.fatal("IO exception:", e);
			loginMap = null;
		} catch (ClassNotFoundException e) {
			// This should really never happen
			System.out.println("Sorry, the bank is not currently dealing with your problems.");
			logger.fatal("UserLogin class not found during deserialization", e);
			loginMap = null;
		}
		
		// If opening the loginMap was successful
		// Use a Scanner
		if (loginMap.equals(null)) return null;

		System.out.println("Welcome to Revbank! Please select one of our great and wonderful options:");
		System.out.println("1. Login");
		System.out.println("2. Create an Account");
		System.out.println("3. Exit");
		input = s.next();

		switch (input) {
			case "1": 
				// Check the users map to see if this user exists
				returnUser = loginMap.getUser(s.next());
				if (returnUser.equals(null)) {
					// Try again to get a user
					System.out.println("Oh nooo! It looks like that's not a valid username.");
					System.out.println("We here at Revbank always want to make sure that you feel like we care.");
					System.out.println("Why don't you try logging in again but this time with an actual username?");
					System.out.println("You can also create a new account if you'd like to join Revbank!");
					return getCurrentUser(s);
				}

				return returnUser;

			case "2":
				// Create a new user and add them to the loginMap
				System.out.println("You should enter a username now! A good username is one that you will remember");
				inputUsername = s.next();
				
				System.out.println("You should also enter a password. A good password is one you probs won't remember");
				inputPassword = s.next();
				
				System.out.println("Oh boy! Of all the passwords anyone has ever come up with, that was one of them.");

				returnUser = new User(inputUsername, inputPassword);
				
				// Make sure the username isn't already taken
				if (loginMap.addLogin(returnUser)) {
					return returnUser;
				}

				System.out.println("Someone is already using that username.");
				System.out.println("Either try again with a new username");
				System.out.println("or login using the one you already have you dumb dumb");
				return getCurrentUser(s);

			default:
				System.out.println("If you didn't want to use Revbank, why did you come to Revbank? Are you stupid?");
				return null;
		}
	}
	
	private static void menu (User menuUser) {
		// Use a scanner class to give the user a chance to interact with the bank including log out
		// If the user is an administrator give them special menu options
	}
}
