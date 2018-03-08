package com.revature.BankingApplicationII;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.revature.banking.accounts.Account;
import com.revature.banking.accounts.AccountInvalidException;
import com.revature.banking.accounts.ApprovalQueue;
import com.revature.banking.accounts.BadWithDrawalException;
import com.revature.banking.login.UserLogin;

public class UserInterface {
    private static final Logger logger = LogManager.getLogger(UserInterface.class);
    private static UserLogin masterLoginTable;
    private static ApprovalQueue pendingAccounts;

    public static void main (String[] args) {
        Scanner s;
        s = new Scanner(System.in);
        // Greet the user with the chance to log in or create an account

        // Load the loginMap from a file
        try (FileInputStream file = new FileInputStream("loginMap.ser");
                ObjectInputStream loginMapStream = new ObjectInputStream(file)){
            masterLoginTable = (UserLogin) loginMapStream.readObject();
        } catch (FileNotFoundException e) {
            // This just means we need to create a new loginMap
            masterLoginTable = new UserLogin();
        } catch (IOException e) {
            /* This is less recoverable.
             * It means that the operating system letting us have files
             */
            System.out.println("Sorry, the bank is not currently open. Please leave");
            logger.fatal("IO exception:", e);
            masterLoginTable = null;
        } catch (ClassNotFoundException e) {
            // This should really never happen
            System.out.println("Sorry, the bank is not currently dealing with your problems.");
            logger.fatal("UserLogin class not found during deserialization", e);
            masterLoginTable = null;
        }

        User currentUser = getCurrentUser(s, masterLoginTable);

        // If no current user was fetched we don't want to do any of this
        // Offer the user a menu
        if(!(currentUser.equals(null))) {
            menu(currentUser, s);
        }
        
        // Save the account queue and the logintable to file
        try (FileOutputStream file = new FileOutputStream("loginMap.ser");
        		ObjectOutputStream loginMapStream = new ObjectOutputStream(file)) {
        	loginMapStream.writeObject(masterLoginTable);
        } catch (FileNotFoundException e) {
			logger.fatal("Unable to open file for writing");
		} catch (IOException e) {
			logger.fatal("Unable to open file for writing, but even worse this time");
		}
        
        try (FileOutputStream file = new FileOutputStream("pendingAccounts.ser");
        		ObjectOutputStream loginMapStream = new ObjectOutputStream(file)) {
        	loginMapStream.writeObject(pendingAccounts);
        } catch (FileNotFoundException e) {
			logger.fatal("Unable to open file for writing");
		} catch (IOException e) {
			logger.fatal("Unable to open file for writing, but even worse this time");
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
            	// Get a user name and password from the user
            	System.out.println("What, exactly, if anything, is your username?");
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
                applyForAccount(menuUser);

            case "2":
                // Let the user withdraw from an existing account
                withdrawFromAccount(menuUser);

            case "3":
                // Let the user make a deposit into one of their accounts
            	depositToAccount(menuUser);

            case "4":
                // Let the user transfer money to another user's account
                makeATransfer(menuUser);

            default:
                if (userIsAdmin) {
                    switch (input) {
                        case "6":
                            // Do a username lookup
                        	userLookup(menuUser);

                        case "7":
                            // Work on the approval queue
                        	doApprovals(menuUser);

                        default:
                            // We're done here
                    }
                } else {
                    // We're done here
                }
        }
    }

    private static void doApprovals(User menuUser) {
    	Account approvalAccount;
    	Scanner s = new Scanner(System.in);
    	String approveWithUpwardInflection;
		// Get the pending accounts from a file
		try (FileInputStream file = new FileInputStream("pendingAccounts.ser");
		        ObjectInputStream pendingAccountStream = new ObjectInputStream(file)){
		    try {
				pendingAccounts = (ApprovalQueue) pendingAccountStream.readObject();
			} catch (ClassNotFoundException e) {
				logger.fatal(e);
			}
		} catch (FileNotFoundException e) {
			System.out.println("There are currently no accounts to approve");
		} catch (IOException e) {
			System.out.println("The bank is having serious issues. You're probably to blame");
			logger.fatal(e);
		}
		
		do {
			approvalAccount = pendingAccounts.checkNextAccount();
			if (approvalAccount != null) {
				System.out.println("Approve new " + approvalAccount.getAccountType() + " account?");
				System.out.println("1. Yes");
				System.out.println("2. No");
				approveWithUpwardInflection = s.next();
				switch (approveWithUpwardInflection) {
				case "1":
					approvalAccount.approveAccount();
					break;
				case "2":
					approvalAccount.rejectAccount();
					break;
				default:
					break;
				}
			}
		} while (approvalAccount != null);
	}

	private static void userLookup(User menuUser) {
    	Scanner s = new Scanner(System.in);
    	String username;
    	String action;
    	User roob;
    	String[] roobAccounts;
    	
    	// Give the almighty power to do what must be done
    	System.out.println("Please enter the username of the unsuspecting roob you wish to screw over");
    	roob = masterLoginTable.getUser(s.next());
    	
    	if (roob.equals(null)) {
    		System.out.println("This is not the form of incompetence we expect out of employees at Revbank! Get out!");
    		return;
    	}
    	
    	System.out.println("That chump! What should we do to them?");
    	System.out.println("1. Get a list of all of their accounts and balances");
    	System.out.println("2. Delete them. Lulz");
    	System.out.println("3. Plot their demise");
    	action = s.next();
    	
    	switch (action) {
    	case "1":
    		roobAccounts = (String[]) roob.getAccountNames().toArray();
    		for (int i = 0; i < roobAccounts.length; i++) {
    			System.out.println(roob.getUsername() + " has " + roob.getAccount(roobAccounts[i]).getBalance() + " in " + roobAccounts[1]);
    		}
    		break;
    	case "2":
    		masterLoginTable.removeUser(roob.getUsername());
    		break;
    	default:
    		System.out.println("Soon...");
    		break;
    	}
	}

	private static void applyForAccount(User applicant) {
        // Use a scanner to get input from the user
        Scanner s = new Scanner(System.in);
        String accountType;
        Double initialAmount;
        String jointAccount;
        String jointAccountType;
        User jointUser;
        boolean done = false;
        
        System.out.println("Revbank understands that humans partner with other humans. You can open a joint account");
        System.out.println("If that's something you would like to do please press other 'y' or 'n' and then enter");
        jointAccount = s.next();
        
        switch (jointAccount) {
            case "y":
            case "Y":
                System.out.println("Can do! Let's start by getting the user name for your partner. Please type that now!");
                jointAccount = s.next();
                
                // In order to check if that's a legitimate user, check the loginmap
                jointUser = masterLoginTable.getUser(jointAccount);
                if (!jointUser.equals(null)) {
                    System.out.println("I'm sure your partner is very excited about this account! It should be fun");
                    
                    // Make sure the account can be added to the partner's account map
                    System.out.println("What sort of account was it that you were planning on putting your monies into?");
                    jointAccountType = s.next();
                    
                    // The both users can't already have this type of account
                    if(jointUser.getAccount(jointAccount).equals(null) && applicant.getAccount(jointAccount).equals(null)){
                        System.out.println("How many monies are you and your partner going to put into this account?");
                        jointAccount = s.next();
                        applicant.applyForJointAccoint(jointUser, jointAccountType, Double.parseDouble(jointAccount));
                    } else {
                        System.out.println("Uh oh! Looks like you already have one of those! Maybe start a new account");
                        done = true;
                        menu(applicant, s);
                    }
                } else {
                    System.out.println("Hmmm... Are you sure that's a person? I'm not convinced that's a person");
                }
        }

        if (!done) {
            System.out.println("Here at Revbank, we care about you! Making sure you get the care you need is important");
            System.out.println("Now would be a good time for you to enter what kind of account you would like to open");
            accountType = s.next();

            System.out.println("Wowie! That's definitely a great kind of account to open. Tell all your friends about it!");
            System.out.println("But how much do you want to put into it? We at Revbank recommend as much as possible");
            initialAmount = Double.parseDouble(s.next());

            System.out.println("We can put that much money in your account! We promise that nothing sinister will happen");
            System.out.println("Please give our associates some time to look over your account and make sure it's legit");
            applicant.applyForAccount(accountType, initialAmount);
            
            // Add this account to the account Queue
            addToAccountQueue(applicant.getAccount(accountType));

            menu(applicant, s);
        }
    }

    private static void withdrawFromAccount(User brokePerson) {
        // User a scanner to get input from the user
        Scanner s = new Scanner(System.in);
        String accountType;
        Double withdrawalAmount;
        Account withdrawalAccount;

        System.out.println("Oh no! Are you taking money away from Revbank? That's probably okay.");
        System.out.println("Which of the accounts that you have here at Revbank would you like to withdraw it from?");
        accountType = s.next();

        // Get the account
        withdrawalAccount = brokePerson.getAccount(accountType);
        System.out.println("You have " + withdrawalAccount.getBalance() + " monies in that account");
        System.out.println("How many of our --AHEM-- your monies would you like to take away from us?");
        withdrawalAmount = Double.parseDouble(s.next());
        
        System.out.println("Okay! Here's your money I guess! Next time you should give us money to keep longer");
        try {
			withdrawalAccount.makeWithdrawal(withdrawalAmount);
			System.out.println("It's done! What's next for an intrepid young customer like you? Stashing money maybe?");
		} catch (AccountInvalidException e) {
			System.out.println("Oh no! We don't have money that is yours there. Maybe you should give us money");
			menu(brokePerson, s);
		} catch (BadWithDrawalException e) {
			System.out.println("Now listen here. We try to be nice here at Revbank... But you've pushed us too far.");
		}
    }
    
    private static void addToAccountQueue (Account newAccount) {
    	ApprovalQueue accountsToBeApproved = null;

    	// Load existing accounts from file
        try (FileInputStream file = new FileInputStream("accountQueue.ser");
                ObjectInputStream accountQueueFile = new ObjectInputStream(file)) {
            accountsToBeApproved = (ApprovalQueue) accountQueueFile.readObject();
            accountsToBeApproved.addNewAccount(newAccount);
        } catch (FileNotFoundException e) {
            // This is actually okay. It just means we need to create a new loginMap
            accountsToBeApproved = new ApprovalQueue();
        } catch (IOException e) {
            // This is less recoverable. It means that the operating system letting us have files
            System.out.println("Sorry, the bank is not currently open. Please leave");
            logger.fatal("IO exception:", e);
        } catch (ClassNotFoundException e) {
            // This should really never happen
            System.out.println("Sorry, the bank is not currently dealing with your problems.");
            logger.fatal("ApprovalQueue class not found during deserialization", e);
        } finally {
			// Now that we're leaving the scope where accountsToBeApproved was declared in, let's serialize it back to file
			try (FileOutputStream file = new FileOutputStream ("accountQueue.ser");
					ObjectOutputStream accountQueueFile = new ObjectOutputStream(file)) {
				accountQueueFile.writeObject(accountsToBeApproved);
			} catch (FileNotFoundException e) {
				System.out.println("The bank is having issues with account creations");
				logger.fatal("File couldn't be created:", e);
			} catch (IOException e) {
				System.out.println("I don't know how to tell you this, but the bank is all broken and sad");
				logger.fatal("IO exception:", e);
			}
        }
    }
    
    public static void depositToAccount(User wealthyPerson) {
        // User a scanner to get input from the user
        Scanner s = new Scanner(System.in);
        String accountType;
        Double depositAmount;
        Account depositAccount;

        System.out.println("Hooray! This doop wants to put money in Revbank! I mean... Thank you for choosing Revbank");
        System.out.println("Which of your accounts would you like to make you feel like you're saving your money?");
        accountType = s.next();

        // Get the account
        depositAccount = wealthyPerson.getAccount(accountType);
        System.out.println("How many of our future --AHEM-- your monies would you like to entrust to us?");
        depositAmount = Double.parseDouble(s.next());
        
        System.out.println("Okay! We promise that we're not holding onto your money for any form of ulterior motive!");
        try {
			depositAccount.makeDeposit(depositAmount);
		} catch (AccountInvalidException e) {
			System.out.println("Oopsiediddle! It looks like that's not an account you have at Revbank.");
		} finally {
			s.close();
		}
    }

    public static void makeATransfer(User kindlyPerson) {
        Scanner s = new Scanner(System.in);
        String accountType;
        Double transferAmount;
        Account startAccount;
        boolean done = false;
        User luckyPerson;

        System.out.println("Oh? So you want to make some of your monies be someone else's monies?");
        System.out.println("That's fine as long as all the monies stay here in Revbank! Revbank is best bank, remember");
        System.out.println("Please use your keyboard to enter which of your accounts you want to transfer from");
        accountType = s.next();

        startAccount = kindlyPerson.getAccount(accountType);
        if (startAccount.equals(null)) {
            System.out.println("You don't have one of those. Be less dumb. Please try again to do something, but be less dumb");
            done = true;
            menu(kindlyPerson, s);
        }

        if (!done) {
            System.out.println("Ooooooh! You have " + startAccount.getBalance() + " in that account");
            System.out.println("How many of your monies do you want give to someone else on a whim?");
            transferAmount = Double.parseDouble(s.next());

            System.out.println("What is the username of the person that you are departing with your monies for?");
            
            luckyPerson = masterLoginTable.getUser(s.next());

            if (luckyPerson.equals(null)) {
                System.out.println("Whoa there! That's not a person. You can't transfer monies to a person that's not a person");
                done = true;
                menu(kindlyPerson, s);
            }

            if (!done) {
                System.out.println("Cool! Now what kind of account were you hoping to transfer into?");
            }
            
            try {
				startAccount.transfer(luckyPerson.getAccount(s.next()), transferAmount);
			} catch (AccountInvalidException e) {
				// TODO Auto-generated catch block
				logger.fatal("There was an issue with on of your accounts");
			} catch (BadWithDrawalException e) {
				// TODO Auto-generated catch block/
				logger.fatal("Maybe make a withdrawal that's actuall possible?");
			}
        }
    }
}
