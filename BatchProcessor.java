/**
* Batch Processor Driver Class
* 
* Class data members
* - String line
* - Scanner input
* - FileWriter output
* - ArrayList<Account> accounts = new ArrayList<Account>()
* - HashSet<Account> acctTransactionHistory = new HashSet<Account>()
*
* Static methods:
*  - void readAccounts (no args)
*  - void readBatches (no args)
*  - void writeAccounts (no args)
*  
*  - Account processTransaction (Account a, String[] batchData)
*  - Account processDeposit (Account a, double Amount)
*  - Account processWithdraw (Account a, double Amount, String owner)
*  - Account processTransfer (Account a, Account b, double Amount, String owner)
*  - Account processClose (Account a, String owner)
*  
* Main Method
* 	- read the data from the accounts.txt file, save the data in accounts ArrayList
* 	- reads the batch file, process all transactions
* 	- overwrite the accounts.txt file with updated versions of the accounts
* 
* @author Rowan and Fredrik 
* @date 04/11/2023
* CMS 270 Assignment 2
*/

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;

public class BatchProcessor {
	
	// class data members
	public static String line;
	public static Scanner input;
	public static FileWriter output;
	 // holds accounts
	public static ArrayList<Account> accounts = new ArrayList<Account>();
	// holds unordered set of accounts based on the transaction history
	// this includes open and closed accounts
	public static HashSet<Account> acctTransactionHistory = new HashSet<Account>();
	// static variable for a single line of batch data
	public static String[] batchData;
	
	/*
	 * Opens accounts.txt and reads the data in the accounts ArrayList
	 */
	public static void readAccounts() {
		try {
			// Read from accounts.txt
	    	File f1 = new File("accounts.txt");
	    	input = new Scanner(f1); 
	    	// Read through the whole file as long as there is a next line
	    	while (input.hasNextLine()) {
	    		// go to next line
	    		line = input.nextLine(); 
	    		//splits on the blank space
	    		String[] accountsData = line.split(" "); 
	    		
	    		// Account number
	    		int id = Integer.parseInt(accountsData[0]);
	    		// Account type
	    		String type = accountsData[1];
	    		// Account Owner
	    		String name = accountsData[2] + ' ' + accountsData[3];
	    		// Account Balance
	    		double balance = Double.parseDouble(accountsData[4]);
			
	    		// use switch case to determine which type of account to create
	    		// in the accounts ArrayList
	    		switch (type) {
					case "C": {
						// Create a Checking account
						accounts.add(new CheckingAccount(balance, id, name));
					}
						break;
					case "I": {
						// Create an Interest Bearing Checking account
						accounts.add(new IBCAccount(balance, id, name));
					}
						break;
					case "S": {
						// Create a Savings account
						accounts.add(new SavingsAccount(balance, id, name));
					}
						break;
	    		}
	    	}
	    	
	    	 // close input file
	    	input.close();
	    	// catch clause for file
		} catch (FileNotFoundException e) {
			System.out.println("Error reading accounts.txt");
			e.printStackTrace();  
		}
	}

	/*
	 * Opens batch.txt and identifies the specified account based on account number
	 * Processes the transaction with this account number and the rest of the data
	 * from the batchData line
	 * add the updated account to an account transaction history HashSet
	 * HashSets contain only one copy of elements
	 */
	public static void readBatches() {
		try {
			// Read from batch.txt
	    	File f2 = new File("batch.txt");
	    	input = new Scanner(f2); 
	    	// Read through the whole file as long as there is a next line
			while (input.hasNextLine()) {
				// go to next line
				line = input.nextLine();
	    		//splits on the blank space
				batchData = line.split(" ");
				
				// Account number
				int id = Integer.parseInt(batchData[1]);

				// the below line identifies the desired source account based on the id from batchData
				// Learned this example through this site => 
				// https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html
				//
				// parallelStream() creates a stream of Account objects that are processed in parallel. 
				// Multiple threads work simultaneously, learned about this sort of threading in 
				// Operating Systems this year.
				// Searches by whether account number == id.
				// findFirst() returns the first instance of this and get() returns that account object.
				Account sourceAccount = accounts.parallelStream().filter(a -> a.getNumber() == id).findFirst().get();
				
				// up to date account object
				Account upToDateAcct = processTransaction(sourceAccount);
				// add these up to date account objects to a HashSet of accounts
				// holds unordered set of accounts based on the transaction history
				// this includes open and closed accounts
				acctTransactionHistory.add(upToDateAcct);
				
			}
			// close input file
			input.close(); 
			// catch clause for file
		} catch (FileNotFoundException e) {
			System.out.println("Error reading batch.txt");
			e.printStackTrace();  
		}
	}
	
	/*
	 * identify and process the transaction on the specified source Account 
	 * takes a specified account and uses line from batchData to process
	 * the necessary transactions and return the updated account
	 */
	public static Account processTransaction(Account acct) {
		Account updatedAccount = null;
		// index 0 is type of transaction
		// switch on type of transaction
		switch (batchData[0]) {
			case "W": {
				// Account balance
				double amount = Double.parseDouble(batchData[2]);
				// Account Owner
				String name = batchData[3] + " " + batchData[4];
				// source of where to process the withdraw
				updatedAccount = processWithdrawal(acct, amount, name);
				return updatedAccount; // return the up-to-date account

			}
			case "D": {
				// Account balance
				double amount = Double.parseDouble(batchData[2]);
				// source of where to process the deposit
				updatedAccount = processDeposit(acct, amount);
				return updatedAccount; // return the up-to-date account

			}
			case "C": {
				// Account Owner
				String name = batchData[2] + " " + batchData[3];
				// source of where to process the close
				// not used but could be used in extension of this project
				updatedAccount = processClose(acct, name);
				return updatedAccount; // return the up-to-date account

			}
			case "T": {
				// Account Owner
				String name = batchData[4] + " " + batchData[5];
				double amount = Double.parseDouble(batchData[3]);
				// Account number for destination account
				int destAccountId = Integer.parseInt(batchData[2]);
				// This line uses the same concept as the example from the readBatches()  function
				// identifies the destination account based on the second account id in batchData
				Account destinationAccount = accounts.stream().filter(a -> a.getNumber() == destAccountId).findAny().get();
				updatedAccount = processTransfer(acct, destinationAccount, amount, name);
				return updatedAccount; // return the up-to-date account
			}
		}
		// should never reach here but if we do,
		// return the source account
		return acct;
	}

	/*
	 * Process deposit and return the account
	 */
	public static Account processDeposit(Account a, double Amount) {
		a.deposit(Amount);
		return a;
	}
	
	/*
	 * Process account closure and return the closed account
	 */
	public static Account processClose(Account a, String owner) {
		if (!a.getOwner().equals(owner) || !a.isClosable()) {
			System.out.println("Account " + a.getNumber() + " could not be closed since it has a negative balance\n");
			return a; // exit from method
		} else {
			a.close();
			// Release account from the accounts ArrayList
			Account closedAcct = accounts.remove(accounts.indexOf(a));
			return closedAcct;
		}
	}

	/*
	 * Process transfer of accounts and return the destination account
	 */
	public static Account processTransfer(Account a, Account b, double Amount, String owner) {
		if (!a.getOwner().equals(owner)) {
			System.out.println(owner + " could not transfer funds from Account " + a.getNumber() + "\n");
			return b; // return destination account
		}
		a.transfer(b, Amount);
		return b; // return destination account
	}

	/*
	 * Process account withdraw and return the account
	 */
	public static Account processWithdrawal(Account a, double Amount, String owner) {
		if (!a.getOwner().equals(owner)) {
			System.out.println(owner + " could not withdraw " + Amount + " from Account " + a.getNumber() + "\n");
		}
		else {
			a.withdraw(Amount);
		}
		return a; // return the account that the withdraw was done on
	}
	
	/*
	 * Write the accounts back to accounts.txt
	 * Iterate over the accounts list, identify the account type,
	 * write the account data back to accounts.txt in correct format
	 */
	public static void writeAccounts() {
	    try {
	    	// Open from accounts.txt for writing
	    	output = new FileWriter("accounts.txt");
	    	// iterate over each account
	        for (Account a : accounts) {
	        	
				char type; // type of account
				// Savings account
				if (a instanceof SavingsAccount)
					// Savings account
					type = 'S';
				// IBC account
				else if (a instanceof IBCAccount)
					type = 'I';
				// Checking Account => instance of checking account but not instance of IBC Account
				else
					type = 'C';
				
				// String being written back to accounts.txt
				String account; 
	            
	            if (type == 'I') {
	                // at the end of the month add all interest to the relevant accounts
	            	// special getBalanceWithVIR is used to calculate the balance for IBCAccount
	            	// downcast an Account object as an IBCAccount object
	                IBCAccount ibcAccount = (IBCAccount) a;
	                account = String.format("%s %s %s %.2f%n", ibcAccount.getNumber(), type, ibcAccount.getOwner(), ibcAccount.getBalanceWithVIR());
	            } else {
	                // handle case where account is not an IBCaccount, so (Checking or Savings)
		            // Create each account line 
		            account = String.format("%s %s %s %.2f%n", a.getNumber(), type, a.getOwner(), a.getBalance());
	            }
	            
	            // Write account to file
	            output.write(account);
	        }
	        // close output file
	        output.close(); 
	        // catch clause for file
	    } catch (IOException e) {
			System.out.println("Error with action of writing to accounts.txt");
			e.printStackTrace();  
	    }
	}
		
	
	/*
	 * Main Method
	 * 
	 * readAccounts() => reads data from the accounts.txt file, saves the data in accounts ArrayList
	 * readBatches() => reads the batch file, process all transactions
	 * writeAccounts() => overwrites the accounts.txt file with updated versions of the accounts
	 */
	public static void main(String[] args) {
    	readAccounts();
    	
    	//Check if accounts are properly read
    	//for (Account a : accounts) {
    	//   System.out.println(a);
    	//}
    	//System.out.println();
    	
		readBatches();
		writeAccounts();
		
		//Check if accounts are properly written
    	//for (Account a : accounts) {
	    //	System.out.println(a);
		//}
		//System.out.println();
		//Check the account transaction history of every updated Account in unordered manner b/c HashSet
		// this HashSet contains transaction history of closed accounts and open accounts
    	//for (Account b : acctTransactionHistory) {
    	//    System.out.println(b);
    	//}
	}
}