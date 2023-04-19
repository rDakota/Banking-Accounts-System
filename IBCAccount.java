/**
* Interest bearing Checking Account
* 	extends CheckingAccount class
* 
* Data members: 
*  - static final int MONTHLY_CHECK_LIMIT: set at 40
*  - double varIntRate
*  - static final double MIN_BALANCE: set at 2000.00
*  - static final double MAINTENANCE_FEE: set at 25.00
*  - static final double IBC_OVERDRAFT_FEE: set at 25.00
* 
* Constructor method:
*  - takes balance, number, and owner
*  - call super with those args
* 
* Instance methods:
*  - getters
*  - double getBalance (no args) (includes variable interest rate)
*  - void withdraw method (double amount) overrides
*  - void deposit method (double amount) overrides
*  - String toString method
*  
* @author Rowan and Fredrik 
* @date 04/11/2023
* CMS 270 Assignment 2
*/
public class IBCAccount extends CheckingAccount {
	
	private double  varIntRate; // variable interest rate
	// these never change and apply to all IBC Accounts
	private static final int MONTHLY_CHECK_LIMIT = 40;
	private static final double MIN_BALANCE = 2000.00;
	private static final double MAINTENANCE_FEE = 25.00;
	private static final double IBC_OVERDRAFT_FEE = 25.00;

	
	// -------------------------------------------------------
	// Constructor
	public IBCAccount(double balance, int number, String owner) {
		super(balance, number, owner);
		// set the correct variable interest rate
		if (balance <= 5000) {
			this.varIntRate = 0.002;
		}
		// balance of over 5000 earn 1%
		else if (balance <= 20000) {
			this.varIntRate = 0.01;
		}
		// balance of over 20000 earn 2%
		else if (balance <= 75000) {
			this.varIntRate = 0.02;
		}
		// balance of over 750000 earn 5%
		else {
			this.varIntRate = 0.05;
		}
	}
	
	// -------------------------------------------------------
	// Getters and Setters
	public int getMonthlyCheckLimit() {
		return MONTHLY_CHECK_LIMIT;
	}

	public double getMinBalance() {
		return MIN_BALANCE;
	}

	public double getMaintenanceFee() {
		return MAINTENANCE_FEE;
	}

	public double getIBCOverdraftFee() {
		return IBC_OVERDRAFT_FEE;
	}
	
	public double getVarIntRate() {
		// The minimum interest earned per month is 0.2% or 0.002
		return varIntRate;
	}
	
	public void setVarIntRate(double varIntRate) {
		this.varIntRate = varIntRate;
	}

	// get the balance with the Variable Interest added on
	public double getBalanceWithVIR() {
		// set balance to include variable interest rate
		setBalance((getBalance() * varIntRate) + getBalance());
		return this.getBalance();
	}

	// -------------------------------------------------------
	// Transaction Methods
	public void withdraw(double amount) {
		// Can't withdraw more than current savings
		if (getChecksUsed() == MONTHLY_CHECK_LIMIT) {
			return;
		}

        if (getBalance() - amount < 0) {
            //overdraft fee if go below 0
            setBalance(getBalance() - IBC_OVERDRAFT_FEE);
        }
        // if the withdraw causes balance to go below 2000
		if (getBalance() - amount < MIN_BALANCE) {
			// Maintenance fee is per transaction
			setBalance(getBalance() - MAINTENANCE_FEE);
		}
		
		// execute the withdraw
		// increment the checks used
		setBalance(getBalance() - amount);
		setChecksUsed(getChecksUsed() + 1);
	}
	
	
	public void deposit(double amount) {
		if (getChecksUsed() == MONTHLY_CHECK_LIMIT) {
			return;
		}
		// execute the deposit
		// increment the checks used
		setBalance(getBalance() + amount);
		setChecksUsed(getChecksUsed() + 1);
	}
	
	// -------------------------------------------------------
	// toString Method for Account
    public String toString() {
    	StringBuilder sb = new StringBuilder("");
    	sb.append("Type: Interest Bearing Checking, " + super.toString() 
    	+ "Max Checks: " + MONTHLY_CHECK_LIMIT + ", Used: " + getChecksUsed() + ", Variable Interest Rate: " + varIntRate);
    	return sb.toString();
    }
}

