package com.revature.banking.login;

import java.io.Serializable;
import java.util.HashMap;

import com.revature.BankingApplicationII.User;

public class UserLogin implements Serializable {
	/**
	 * We want to store all of the Users that have accounts for later
	 */
	private static final long serialVersionUID = -8997731385300182094L;

	// Store user logins in the form of a map
	private HashMap<String, User> logins;
	
	public UserLogin () {
		this.logins = new HashMap<String, User>();
	}

	public User getUser (String possibleUser) {
		return this.logins.get(possibleUser);
	}

	public boolean addLogin (User newUser) {
		// Check to make sure the username isn't already in use
		if (logins.get(newUser.getUsername()) == null) {
			logins.put(newUser.getUsername(), newUser);
			return true;
		}
		
		return false;
	}
	
	public void removeUser (String ridUser) {
		System.out.println("Lol");
		logins.remove(ridUser);
	}
}
