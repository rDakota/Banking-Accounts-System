/**
* Savings Account
* 	extends Account class
* 
* Data members: 
*  - boolean maintenanceFeeCharged: false
*  - boolean overdraftFeeCharged: false
*  - static final double MIN_BALANCE: set at 100.00
*  - static final double MAINTENANCE_FEE: set at 5.00
*  - static final double OVERDRAFT_FEE: set at 60.00
* 
* Constructor method:
*  - takes balance, number, and owner
*  - call super with those args
* 
* Instance methods:
*  - getters and Setters
*  - void withdraw method (double amount) overrides
*  - void deposit method (double amount) overrides
*  - String toString method
* 
* @author Rowan and Fredrik 
* @date 04/11/2023
* CMS 270 Assignment 2
*/
public class SavingsAccount extends Account {

	private boolean maintenanceFeeCharged = false;
	private boolean overdraftFeeCharged = false;
	// these never change and apply to all Savings Accounts
	private static final double MIN_BALANCE = 100.00;
	private static final double MAINTENANCE_FEE = 5.00;
	private static final double OVERDRAFT_FEE = 60.00;

	// -------------------------------------------------------
    // Constructor
	public SavingsAccount(double balance, int number, String owner) {
		super(balance, number, owner);
	}
	
	// -------------------------------------------------------
    // Getters and Setters
	public boolean isMaintenanceFeeCharged() {
		return maintenanceFeeCharged;
	}

	public void setMaintenanceFeeCharged(boolean maintenanceFeeCharged) {
		this.maintenanceFeeCharged = maintenanceFeeCharged;
	}

	public boolean isOverdraftFeeCharged() {
		return overdraftFeeCharged;
	}

	public void setOverdraftFeeCharged(boolean overdraftFeeCharged) {
		this.overdraftFeeCharged = overdraftFeeCharged;
	}

	public static double getMinBalance() {
		return MIN_BALANCE;
	}

	public static double getMaintenanceFee() {
		return MAINTENANCE_FEE;
	}

	public static double getOverdraftFee() {
		return OVERDRAFT_FEE;
	}
	

	// -------------------------------------------------------
    // Transaction methods that override ones in Account
	public void withdraw(double amount) {
		// cant withdraw more than current savings
        if (amount > getBalance()) {
            // charge overDraft free once per month
        	// we only care about one month b/c
        	// batch file is only one month
            if (!overdraftFeeCharged) {
				setBalance(getBalance() - OVERDRAFT_FEE);
				overdraftFeeCharged = true;
			}
         
        }
        // if the withdraw causes balance to go below 100
		if (getBalance() - amount < MIN_BALANCE)
			// The Maintenance fee is charged once per month
			// The batch file contains transactions for one month
			// so simply need to check if the maintenance fee is charged
			// and set to true if it has been
			if (!maintenanceFeeCharged) {
				setBalance(getBalance() - MAINTENANCE_FEE);
				maintenanceFeeCharged = true;
			}
		// execute the withdraw
		setBalance(getBalance() - amount);
	}
	
	public void deposit(double amount) {
		setBalance(getBalance() + amount);
	}


	// -------------------------------------------------------
    // toString method
    public String toString() {
    	StringBuilder sb = new StringBuilder("");
    	sb.append("Type: Savings, " + super.toString() + "Maintenance Fee charged?: " 
    	+ maintenanceFeeCharged + ", Overdraft Fee charged?: " + overdraftFeeCharged);
    	return sb.toString();
    }
}