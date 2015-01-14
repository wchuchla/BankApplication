package com.luxoft.bankapp.model;

import com.luxoft.bankapp.exception.OverDraftLimitExceededException;
import com.luxoft.bankapp.model.info.CheckingAccountInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;

public class CheckingAccountTest {
	private CheckingAccount sut;

	@Before
	public void createCheckingAccountWithBalance0AndOverdraft100() {
		sut = newCheckingAccount();
	}


	// test CheckingAccount()
	@Test
	public void testSetDefaultBalanceAndDefaultOverdraftInDefaultConstructor() {
		sut = new CheckingAccount();

		assertEquals(0.0f, sut.getBalance(), 0);
		assertEquals(0.0f, sut.getOverdraft(), 0);
	}


	// test CheckingAccount(float balance)
	@Test
	public void testSetBalanceAndDefaultOverdraftInConstructor() {
		sut = new CheckingAccount(1.0f);

		assertEquals(1.0f, sut.getBalance(), 0);
		assertEquals(0.0f, sut.getOverdraft(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetNegativeBalanceInConstructorThrowsException() {
		sut = new CheckingAccount(-1.0f);
	}


	// test CheckingAccount(float balance, float overdraft)
	@Test
	public void testSetBalanceInConstructor() {
		sut = new CheckingAccount(1.0f, 0);

		assertEquals(1.0f, sut.getBalance(), 0);
	}

	@Test
	public void testSetOverdraftInConstructor() {
		sut = new CheckingAccount(0, 1.0f);

		assertEquals(1.0f, sut.getOverdraft(), 0);
	}

	@Test
	public void testSetBalanceAndOverdraftInConstructor() {
		sut = new CheckingAccount(1.0f, 2.0f);

		assertEquals(1.0f, sut.getBalance(), 0);
		assertEquals(2.0f, sut.getOverdraft(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeBalanceInConstructorThrowsException() {
		sut = new CheckingAccount(-1.0f, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeOverdraftInConstructorThrowsException() {
		sut = new CheckingAccount(0, -1.0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeBalanceAndNegativeOverdraftInConstructorThrowsException() {
		sut = new CheckingAccount(-1.0f, -1.0f);
	}


	// test CheckingAccount(CheckingAccount checkingAccount)
	@Test
	public void testCopyConstructor() {
		CheckingAccount sut2 = new CheckingAccount(sut);

		assertEquals(sut.getId(), sut2.getId());
		assertEquals(sut.getBalance(), sut2.getBalance(), 0);
		assertEquals(sut.getAccountNumber(), sut2.getAccountNumber());
		assertEquals(sut.getOverdraft(), sut2.getOverdraft(), 0);
	}


	// test CheckingAccount(CheckingAccountInfo info)
	@Test
	public void testConstructorFromInfo() {
		CheckingAccountInfo CheckingAccountInfo = new CheckingAccountInfo(sut);
		Account sut2 = new CheckingAccount(CheckingAccountInfo);

		assertEquals(sut.getId(), sut2.getId());
		assertEquals(sut.getBalance(), sut2.getBalance(), 0);
		assertEquals(sut.getAccountNumber(), sut2.getAccountNumber());
	}


	// test setOverdraft(float overdraft)
	@Test
	public void testSetOverdraftInSetOverdraft() {
		sut.setOverdraft(1.0f);

		assertEquals(1.0f, sut.getOverdraft(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeOverdraftInSetOverdraftThrowsException() {
		sut.setOverdraft(-1.0f);
	}


	// test withdraw(float balance)
	@Test
	public void testWithdrawDecreaseBalance() throws OverDraftLimitExceededException {
		float amount = 1;

		sut.withdraw(amount);

		assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE - amount, sut.getBalance(), 0.00001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawNegativeAmountThrowsException() throws OverDraftLimitExceededException {
		sut.withdraw(-1.0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawZeroAmountThrowsException() throws OverDraftLimitExceededException {
		sut.withdraw(0.0f);
	}

	@Test
	public void testWithdrawAmountMoreThanBalance() throws OverDraftLimitExceededException {
		float amount = CHECKING_ACCOUNT_INITIAL_BALANCE + 1;

		sut.withdraw(amount);

		assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE - amount, sut.getBalance(), 0.00001f);
	}

	@Test(expected = OverDraftLimitExceededException.class)
	public void testWithdrawAmountMoreThanBalancePlusOverdraftThrowsException() throws OverDraftLimitExceededException {
		float amount = CHECKING_ACCOUNT_INITIAL_BALANCE + CHECKING_ACCOUNT_OVERDRAFT + 1;

		sut.withdraw(amount);
	}


	// test parseFeed()
	@Test
	public void testParseFeed() {
		Map<String, String> feed = new HashMap<>();
		feed.put("balance", "1.0");
		feed.put("overdraft", "1.0");

		sut.parseFeed(feed);

		assertEquals(1.0, sut.getBalance(), 0.0f);
		assertEquals(1.0, sut.getOverdraft(), 0.0f);
	}


	// test printReport()
	@Test
	public void testPrintReport() {
		final int ID = sut.getId();
		final int ACCOUNT_NUMBER = sut.getAccountNumber();
		final float BALANCE = sut.getBalance();
		final float OVERDRAFT = sut.getOverdraft();

		final String EXPECTED_STRING = "Account type = Checking account, ID = " + ID
				+ ", account number = " + ACCOUNT_NUMBER + ", balance = " + BALANCE
				+ ", overdraft = " + OVERDRAFT;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.printReport();
		final String printReportOutput = byteArrayOutputStream.toString();

		assertEquals("printReport() method does not produce the expected output",
				EXPECTED_STRING, printReportOutput);
	}


	// test toString()
	@Test
	public void testToString() {
		final int ID = sut.getId();

		final String EXPECTED_STRING =
				"CheckingAccount [id=" + ID + ", accountNumber=" + CHECKING_ACCOUNT_ACCOUNT_NUMBER
						+ ", balance=" + CHECKING_ACCOUNT_INITIAL_BALANCE + ", overdraft=" + CHECKING_ACCOUNT_OVERDRAFT + "]";

		assertEquals("toString() method does not produce the expected output",
				EXPECTED_STRING, sut.toString());
	}


	// test equalsContract
	@Test
	public void equalsContract() {
		EqualsVerifier.forClass(CheckingAccount.class)
				.suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
				.verify();
	}
}