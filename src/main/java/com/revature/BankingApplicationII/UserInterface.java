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
		UserLogin loginMap;

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

		User currentUser = getCurrentUser(s, loginMap);
		
		// If no current user was fetched we don't want to do any of this
		// Offer the user a menu
		if(!currentUser.equals(null)) {
			menu(currentUser, s);
		}
		
		System.out.println("Thank you for visiting Revbank! Revbank! It's the bankiest!");
		System.out.println("If you did not mean to leave Revbank, then we're kicking you out for being a bad person");
	}
	
	static User getCurrentUser(Scanner s, UserLogin loginMap) {
		// We will need to check the users map for user logins
		User returnUser = null;
		
		// We will need to store a user's input for later
		String input;
		String inputUsername;
		String inputPassword;
		
		
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
					return getCurrentUser(s, loginMap);
				}

				logger.info("Returning a user with username " + returnUser.getUsername());

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
					logger.info("Comitted a user with username " + loginMap.getUser(returnUser.getUsername()).getUsername());
					logger.info("Returning a user with user with username " + returnUser.getUsername());
					return returnUser;
				}

				System.out.println("Someone is already using that username.");
				System.out.println("Either try again with a new username");
				System.out.println("or login using the one you already have you dumb dumb");
				return getCurrentUser(s, loginMap);

			default:
				System.out.println("If you didn't want to use Revbank, why did you come to Revbank? Are you stupid?");
				return null;
		}
	}
	
	private static void menu (User menuUser, Scanner s) {
		// We will need to get input from the user
		String input;
		
		// Different behaviors based on the user's authorization levels
		boolean userIsAdmin = menuUser.getUsername().equals("BankAdministrator");
		// Use a scanner class to give the user a chance to interact with the bank including log out
		// If the user is an administrator give them special menu options
		System.out.println("It's time for some moooore options! You like options, don't you? Everyone loves options");
		System.out.println("1. Apply for one of our great and wonderful accounts");
		System.out.println("2. Withdraw from one of your favorite accounts");
		System.out.println("3. Make a deposit into an account that you feel doesn't get nearly all the love it needs");
		System.out.println("4. Make a transfer from one of your accounts to another person's account");
		System.out.println("5. Log out");
		
		// Special administrative priviledges
		if (userIsAdmin) {
			System.out.println();
			System.out.println("Special options for special people with special powers");
			System.out.println("6. Look up the username of one of your marks");
			System.out.println("7. Play god and approve or reject requests to open accounts on a whim");
			System.out.println("8. Make an example of one of your customers by closing their account for all to see");
		}
		
		// Get input from the user
		input = s.next();
		switch (input) {
			case "1":
				// Let the user apply for an account

			case "2":
				// Let the user withdraw from an existing account
				
			case "3":
				// Let the user make a deposit into one of their accounts

			case "4":
				// Let the user transfer money to another user's account
				
			default:
				if (userIsAdmin) {
					switch (input) {
						case "6":
							// Do a username lookup
							
						case "7":
							// Work on the approval queue
							
						case "8":
							// Delete an account
							
						default:
							// We're done here
					}
				} else {
					// We're done here
				}
		}
	}
}
