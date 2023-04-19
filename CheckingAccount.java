/**
* CheckingAccount class
* 	extends Account class
* 
* Data members: 
*  - static final int MONTHLY_CHECK_LIMIT: set at 20
*  - int checksUsed
*  - static final double OVERDRAFT_FEE: set at 20.00
* 
* Constructor method:
*  - takes balance, number, and owner
*  - call super with those args
* 
* Instance methods:
*  - getters and setters
*  - void withdraw method (double amount) overrides
*  - void deposit method (double amount) overrides
*  - String toString method
* 
* @author Rowan and Fredrik 
* @date 04/11/2023
* CMS 270 Assignment 2
*/
public class CheckingAccount extends Account {

	private int checksUsed = 0;
	// these never change and apply to all Checking Accounts
	private static final int MONTHLY_CHECK_LIMIT = 20;
	private static final double OVERDRAFT_FEE = 20.00;

	// -------------------------------------------------------
	// Constructor
	public CheckingAccount(double balance, int number, String owner) {
		super(balance, number, owner);
	}

	// -------------------------------------------------------
	// Getters and setters
	public int getMonthlyCheckLimit() {
		return MONTHLY_CHECK_LIMIT;
	}
	
	public double getOverdraftFee() {
		return OVERDRAFT_FEE;
	}

	public int getChecksUsed() {
		return checksUsed;
	}

	public void setChecksUsed(int checksUsed) {
		this.checksUsed = checksUsed;
	}
	
	// -------------------------------------------------------
	// Transaction methods
	public void deposit(double amount) {
		if (checksUsed == MONTHLY_CHECK_LIMIT) {
			return; // exit
		}
        // perform deposit
		setBalance(getBalance() + amount);
		checksUsed++;
	}

	public void withdraw(double amount) {
		if (checksUsed == MONTHLY_CHECK_LIMIT) {
			return; // exit
		}
		// if leads to a negative amount then cause overdraft fee
        if (getBalance() - amount < 0) {
            //overdraft fee
            setBalance(getBalance() - OVERDRAFT_FEE);
        }
		// perform withdraw
		setBalance(getBalance() - amount);
		checksUsed++;
	}

	// -------------------------------------------------------
	// toString Method
    public String toString() {
    	
    	// if the type of the account calling this toString method is an instance of the child class IBCAccount,
    	// return super.toString(); => the grandparent method of IBCAccount
    	if (this instanceof IBCAccount) {
    	    return super.toString();
        // else, (it is an instance of Checking Account solely) so return this toString
    	} else {
    	    StringBuilder sb = new StringBuilder("");
    	    sb.append("Type: Checking, " + super.toString() + " Max Checks: " + MONTHLY_CHECK_LIMIT + ", Used: " + checksUsed);
    	    return sb.toString();
    	}
    	
    }

}