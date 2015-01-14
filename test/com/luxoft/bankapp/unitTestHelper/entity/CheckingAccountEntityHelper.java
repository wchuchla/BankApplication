package com.luxoft.bankapp.unitTestHelper.entity;


import com.luxoft.bankapp.model.CheckingAccount;

import static org.junit.Assert.*;

public class CheckingAccountEntityHelper {

	public static final int CHECKING_ACCOUNT_ACCOUNT_NUMBER = 10000002;
	public static final int SECOND_CHECKING_ACCOUNT_ACCOUNT_NUMBER = 10000003;
	public static final String CHECKING_ACCOUNT_TYPE = "C";
	public static final float CHECKING_ACCOUNT_INITIAL_BALANCE = 100;
	public static final float CHECKING_ACCOUNT_OVERDRAFT = 1000;

	private CheckingAccountEntityHelper() {
		throw new UnsupportedOperationException("This class is a helper");
	}

	public static CheckingAccount newCheckingAccount() {
		CheckingAccount checkingAccount =
				new CheckingAccount(CHECKING_ACCOUNT_INITIAL_BALANCE, CHECKING_ACCOUNT_OVERDRAFT);
		checkingAccount.setAccountNumber(CHECKING_ACCOUNT_ACCOUNT_NUMBER);
		return checkingAccount;
	}

	public static CheckingAccount newSecondCheckingAccount() {
		CheckingAccount checkingAccount =
				new CheckingAccount(CHECKING_ACCOUNT_INITIAL_BALANCE, CHECKING_ACCOUNT_OVERDRAFT);
		checkingAccount.setAccountNumber(SECOND_CHECKING_ACCOUNT_ACCOUNT_NUMBER);
		return checkingAccount;
	}

	public static void assertCheckingAccount(CheckingAccount checkingAccount) {
		assertNotNull(checkingAccount);
		assertTrue(checkingAccount.getId() > 0);
		assertEquals(CHECKING_ACCOUNT_ACCOUNT_NUMBER, checkingAccount.getAccountNumber());
		assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE, checkingAccount.getBalance(), 0.0f);
		assertEquals(CHECKING_ACCOUNT_OVERDRAFT, checkingAccount.getOverdraft(), 0.0f);
	}

	public static void assertSecondCheckingAccount(CheckingAccount checkingAccount) {
		assertNotNull(checkingAccount);
		assertTrue(checkingAccount.getId() > 0);
		assertEquals(SECOND_CHECKING_ACCOUNT_ACCOUNT_NUMBER, checkingAccount.getAccountNumber());
		assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE, checkingAccount.getBalance(), 0.0f);
		assertEquals(CHECKING_ACCOUNT_OVERDRAFT, checkingAccount.getOverdraft(), 0.0f);
	}
}