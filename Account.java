/**
* Abstract class for Account
* 
* Data members: 
*  - double balance
*  - int number
*  - String owner
* 
* Constructor method:
* - takes balance, number, and owner
*  
* Instance methods:
*  - Getters and Setters
*  - (Transaction methods) =>
*  	- void transfer (Account a, double amount)
*  	- void close (no args)
*  	- abstract void withdraw (double amount)
*  	- abstract void deposit (double amount)
*  	- boolean isClosable
*  	- String toString
* 
* @author Rowan and Fredrik 
* @date 04/11/2023
* CMS 270 Assignment 2
*/

public abstract class Account {
	private double balance;
	private int number;
	private String owner;

	// -------------------------------------------------------
	// Constructor
	public Account(double balance, int number, String owner) {
		this.balance = balance;
		this.number = number;
		this.owner = owner;
	}

	// -------------------------------------------------------
	// Getters and Setters
	public double getBalance() {
		return balance;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public int getNumber() {
		return number;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	// -------------------------------------------------------
	// Transaction methods
	
	// Transfer
	public void transfer(Account a, double amount) {
		// Transfer is the same for all accounts 
		// Transfers mean => withdrawing from one account and depositing to another
		withdraw(amount);
		a.deposit(amount);
	}
	
	// Close
	public void close() {
		// The owner gets all of the money so the account balance is now 0
		// This bypasses any weird maintenance fee stuff
		setBalance(0);
	}
	
	// Withdraw
	public abstract void withdraw(double amount);
	// Deposit
	public abstract void deposit(double amount);

	
	// -------------------------------------------------------
	// is Closable method
	public boolean isClosable() {
		if (balance >= 0)
			return true;
		return false;
	}
	
	// -------------------------------------------------------
	// toString Method for Account
    public String toString() {
    	StringBuilder sb = new StringBuilder("");
    	sb.append("Owner: " + owner + ", ID Number: " + number + ", Balance: " + balance + ", ");
    	return sb.toString();
    }

}