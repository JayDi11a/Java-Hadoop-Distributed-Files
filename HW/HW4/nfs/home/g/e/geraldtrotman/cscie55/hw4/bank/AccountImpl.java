package cscie55.hw4.bank;

/**
 * This is the implementation of our Account Interface that builds methods for 
 * the Account such as id, balance, deposit, and withdrawl.
 * @author Gerald Trotman
 * @version 1.0 November 1, 2015
 *
 */


/**
 * The AccountImpl is an implementaion of the Account Interface that contain the methods for the functionality of an account
 * @see Account
 *
 */
public class AccountImpl implements Account {

	private long balance = 0;
	private final int id;

	public AccountImpl(int id) {
		this.id = id;
	}
	
	/**
	 * This is a simple method that gives back the id of the account holder
	 * @return id id represents the unique identifier of the account
	 */
	@Override
	public int id() {
		return id;
	}

	/**
	 * This method gives back the balance on the account.
	 * @return balance this represents the final amount on the account
	 *
	 */
	@Override
	public long balance() {
		return balance;
	}

	/**
	 * This method gets passed long numeric values representing dollars/cents 
	 * that get added to the account balance or throws an error if it is not of the correct type
	 * or the balance itself is less than 0.
	 * @param amount the amount is a long numeric value that represents money being added to the account
	 */
	@Override
	public void deposit(long amount) {
		if(amount <= 0){
			throw new IllegalArgumentException(String.format("%s Is An Incorrect Value for a Deposit" + "(0 or Negative Number)", amount) );
		}

		balance +=amount;

		//long is signed, if the balance overflows we need to notify
		if(balance < 0){
			throw new RuntimeException("Overflow Detected on Account: " + this.id());
		}
	}

	/**
	 * This method gets passed a long numeric value that represents dollars/cents that get subtracted
	 * from the account balance or throws an error if there is nothing in the account to be withdrawn
	 * @param amount the amount is a long numeric value that represents money being subtracted from the account
	 * @throws InsufficientFundsException get called if the account balance is 0 and a withdrawal is being attempted
	 */
	@Override
	public void withdraw(long amount) throws InsufficientFundsException {
		if(amount <= 0){
			throw new IllegalArgumentException(String.format("%s Is An Incorrect Value for A Withdraw" + "(0 or Negative Number)", amount) );
		}

		if(balance - amount < 0){
			throw new InsufficientFundsException(this, amount);
		}

		balance -= amount;
	}
}
