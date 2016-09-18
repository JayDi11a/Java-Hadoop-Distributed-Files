package cscie55.hw4.bank;

/**
 * This class implements the Bank interface which creates methods typically peformed by a bank
 * @author Gerald Trotman
 * @version 1.0 November 1, 2015
 *
 */



import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The BankImpl Class is an impemenation of the Bank Interface that contains methods such as creating an account for a client, 
 * performing transfers for a customer, obtaining a customer's total balance.
 * @see Bank
 *
 */

public class BankImpl implements Bank {

	/**
	 * This method adds accounts to an instantiated hash map of accounts that locates them by the unique identifier id and
	 * throws an exception if the account being created already exists.
	 * @param account represents an instance of an account object stored inside the hash map
	 * @throws DuplicateAccountException this gets called in the event of the acccount added already existed
	 */ 
	Map<Integer,Account> existingAccounts = new ConcurrentHashMap<Integer,Account>();

	@Override
	public void addAccount(Account account) throws DuplicateAccountException {
		if(existingAccounts.containsKey(account.id())){
			throw new DuplicateAccountException(account.id());
		}


		existingAccounts.put(new Integer(account.id()), account);
	
	}

	/**
	 * This method performs withdrawl from one account and deposit in another account to simulate 
	 * separate threads and demonstrates what happends when synchronization is missing 
	 * @param fromId the account id of which the transfer is coming from
	 * @param toId the account id to whihc the transfer is going to
	 * @param amount the amount value that is being passed to the transfer method
	 * @throws InsufficientFundsException this gets thrown in attempting to transfer funds from an account with a 0 balance 
	 * 
	 */
	@Override
	public void transferWithoutLocking(int fromId, int toId, long amount) throws InsufficientFundsException {
		existingAccounts.get(fromId).withdraw(amount);
		existingAccounts.get(toId).deposit(amount);
	}

	/**
	 * This method performs withdrawl from one account and deposit in another that performs a synchronization on the entire 
	 * bank object but does not provide concurrency.
	 * @param fromId the account id of which the transfer is coming from
	 * @param toId the account id to whihc the transfer is going to
	 * @param amount the amount value that is being passed to the transfer method
	 * @throws InsufficientFundsException this gets thrown in attempting to transfer funds from an account with a 0 balance 
	 * 
	 */
	@Override
	public void transferLockingBank(int fromId, int toId, long amount) throws InsufficientFundsException {
		synchronized(this){
			existingAccounts.get(fromId).withdraw(amount);
			existingAccounts.get(toId).deposit(amount);
		}
	}


	/**
	 * This method performs withdrawl from one account and deposit in another isolating the synchronization to the two accounts 
	 *  providing better concurrency.
	 * @param fromId the account id of which the transfer is coming from
	 * @param toId the account id to whihc the transfer is going to
	 * @param amount the amount value that is being passed to the transfer method
	 * @throws InsufficientFundsException this gets thrown in attempting to transfer funds from an account with a 0 balance 
	 * 
	 */
	@Override
	public void transferLockingAccounts(int fromId, int toId, long amount) throws InsufficientFundsException {
		Account fromAccount = existingAccounts.get(fromId);
		Account toAccount = existingAccounts.get(toId);

		synchronized(fromAccount){
			fromAccount.withdraw(amount);
		}
		synchronized(toAccount){
			toAccount.deposit(amount);
		}
	}

	
	/**
	 * This method returns the total balance across various accounts of the same account id
	 * @return total this value gets returned that adds up the entire balance across several potential accounts
	 * 
	 */ 					
	@Override
	public long totalBalances() {
		long total = 0;
		for(Account account : existingAccounts.values()) {
			total += account.balance();
		}
		return total;
	}

	/**
	 * This method retuns the number of accounts that a given account id may have
	 * @return the method for how many accounts exist for a particular id key.
	 *
	 */
	@Override
	public int numberOfAccounts() {
		return existingAccounts.keySet().size();
	}
}
