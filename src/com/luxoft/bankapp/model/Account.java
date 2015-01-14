package com.luxoft.bankapp.model;

import com.luxoft.bankapp.exception.NotEnoughFundsException;

import java.util.Map;

public interface Account extends Report, Comparable<Account> {

	/**
	 * Deposits the amount on this account
	 *
	 * @param amount the amount to deposit
	 */
	void deposit(float amount);

	/**
	 * Withdraws the amount from this account
	 *
	 * @param amount the amount to withdraw
	 * @throws java.lang.IllegalArgumentException if the specified amount is negative
	 * @throws NotEnoughFundsException
	 */
	void withdraw(float amount) throws NotEnoughFundsException;

	/**
	 * Prints the rounded account balance
	 */
	void decimalValue();

	int getAccountNumber();

	float getBalance();

	int getId();

	void setId(int id);

	String getAccountType();

	void parseFeed(Map<String, String> feed);
}