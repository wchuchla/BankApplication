package com.luxoft.bankapp.model;

import java.io.Serializable;

public abstract class AbstractAccount implements Account, Serializable {

	private static final long serialVersionUID = 1L;
	private static int nextAccountNumber = 10200000;
	@NoDB
	protected int id;
	protected float balance;
	protected int accountNumber;

	public AbstractAccount() {
		balance = 0.0f;
	}

	public AbstractAccount(float balance) {
		if (balance < 0) {
			throw new IllegalArgumentException(
					"Initial balance must be greater than or equal to 0");
		}
		this.balance = balance;
		this.accountNumber = nextAccountNumber++;
	}

	public AbstractAccount(AbstractAccount abstractAccount) {
		balance = abstractAccount.balance;
		accountNumber = abstractAccount.accountNumber;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	@Override
	public void deposit(float amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}
		balance += amount;
	}

	@Override
	public void decimalValue() {
		System.out.println("The rounded account balance is " + Math.round(balance));
	}

	@Override
	public void printReport() {
		System.out.print("ID = " + id + ", account number = " + accountNumber + ", balance = " + balance);
	}

	@Override
	public int compareTo(Account o) {
		return Integer.compare(accountNumber, o.getAccountNumber());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AbstractAccount)) {
			return false;
		}

		AbstractAccount that = (AbstractAccount) o;

		return accountNumber == that.accountNumber && id == that.id;

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + accountNumber;
		return result;
	}
}