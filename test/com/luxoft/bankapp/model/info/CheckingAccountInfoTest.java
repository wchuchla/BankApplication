package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.CheckingAccount;
import org.junit.Before;
import org.junit.Test;

import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;

public class CheckingAccountInfoTest {

	private CheckingAccountInfo sut;
	private CheckingAccount testCheckingAccount;

	@Before
	public void createCheckingAccountWithBalance0AndOverdraft100() {
		testCheckingAccount = newCheckingAccount();
	}


	// test CheckingAccountInfo(CheckingAccount testCheckingAccount)
	@Test
	public void testConstructorWithCheckingAccountParameter() {
		sut = new CheckingAccountInfo(testCheckingAccount);

		assertEquals(CHECKING_ACCOUNT_ACCOUNT_NUMBER, sut.getAccountNumber());
		assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE, sut.getBalance(), 0.0f);
		assertEquals(CHECKING_ACCOUNT_OVERDRAFT, sut.getOverdraft(), 0.0f);
	}


	// test CheckingAccountInfo(int accountNumber, float balance, float overdraft)
	@Test
	public void testConstructorWithAccountInfoParameters() {
		sut = new CheckingAccountInfo(testCheckingAccount.getAccountNumber(), testCheckingAccount.getBalance(),
				testCheckingAccount.getOverdraft());

		assertEquals(CHECKING_ACCOUNT_ACCOUNT_NUMBER, sut.getAccountNumber());
		assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE, sut.getBalance(), 0.0f);
		assertEquals(CHECKING_ACCOUNT_OVERDRAFT, sut.getOverdraft(), 0.0f);
	}

	// test toString()
	@Test
	public void testToString() {
		sut = new CheckingAccountInfo(testCheckingAccount);

		final String EXPECTED_STRING = "CheckingAccountInfo [accountNumber=" + CHECKING_ACCOUNT_ACCOUNT_NUMBER + ", " +
				"balance=" + CHECKING_ACCOUNT_INITIAL_BALANCE + ", " + "overdraft=" + CHECKING_ACCOUNT_OVERDRAFT + "]";

		assertEquals("toString() method does not produce the expected output",
				EXPECTED_STRING, sut.toString());
	}

}