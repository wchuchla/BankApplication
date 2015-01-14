package com.luxoft.bankapp.model;

import com.luxoft.bankapp.exception.OverDraftLimitExceededException;
import com.luxoft.bankapp.model.info.CheckingAccountInfo;

import java.util.Map;

public class CheckingAccount extends AbstractAccount {

	private static final long serialVersionUID = 1L;
	private static final String OVERDRAFT = "overdraft";
	private static final String BALANCE = "balance";
	private float overdraft;

	/**
	 * Constructs a new checking account with the default initial balance (0.0) and the default initial overdraft (0.0)
	 */
	public CheckingAccount() {
		overdraft = 0;
	}

	/**
	 * Constructs a new checking account with the specified initial balance and the default overdraft (0.0)
	 *
	 * @param balance the initial balance
	 * @throws java.lang.IllegalArgumentException if the initial balance is negative
	 */
	public CheckingAccount(float balance) {
		super(balance);
	}

	/**
	 * Constructs a new checking account with the specified initial balance and overdraft
	 *
	 * @param balance   the initial balance
	 * @param overdraft the initial overdraft
	 * @throws java.lang.IllegalArgumentException if the initial balance or overdraft is negative
	 */
	public CheckingAccount(float balance, float overdraft) {
		this(balance);
		if (overdraft < 0) {
			throw new IllegalArgumentException("Overdraft must be greater than or equal to 0");
		}
		this.overdraft = overdraft;
	}

	public CheckingAccount(CheckingAccount checkingAccount) {
		super(checkingAccount);
		overdraft = checkingAccount.overdraft;
	}

	/**
	 * Constructs a new checking account from CheckingAccountInfo bean
	 *
	 * @param info - the checking account's info
	 * @throws java.lang.NullPointerException if the specified info is null
	 */
	public CheckingAccount(CheckingAccountInfo info) {
		accountNumber = info.getAccountNumber();
		balance = info.getBalance();
		overdraft = info.getOverdraft();
	}

	public float getOverdraft() {
		return overdraft;
	}

	public void setOverdraft(float overdraft) {
		if (overdraft < 0) {
			throw new IllegalArgumentException("Overdraft cannot be negative.");
		}
		this.overdraft = overdraft;
	}

	@Override
	public String getAccountType() {
		return "Checking";
	}


	/**
	 * @throws OverDraftLimitExceededException if the amount is bigger than current balance + overdraft
	 */
	@Override
	public void withdraw(float amount) throws OverDraftLimitExceededException {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}
		if (balance + overdraft < amount) {
			throw new OverDraftLimitExceededException(amount - balance - overdraft, balance + overdraft);
		}
		balance -= amount;
	}

	@Override
	public void parseFeed(Map<String, String> feed) {
		balance = Float.parseFloat(feed.get(BALANCE));
		overdraft = Float.parseFloat(feed.get(OVERDRAFT));
	}

	@Override
	public void printReport() {
		System.out.print("Account type = Checking account, ");
		super.printReport();
		System.out.print(", overdraft = " + overdraft);
	}

	@Override
	public String toString() {
		return "CheckingAccount [id=" + id + ", accountNumber=" + accountNumber + ", balance=" + balance + ", " +
				"overdraft=" + overdraft + "]";
	}
}