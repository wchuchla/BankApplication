package com.luxoft.bankapp.model;

import com.luxoft.bankapp.exception.NotEnoughFundsException;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AbstractAccountTest {

	private TestingAccount sut1;


	// test AbstractAccount()
	@Test
	public void testSetDefaultBalanceInDefaultConstructor() {
		sut1 = new TestingAccount();

		assertEquals(0.0f, sut1.getBalance(), 0);
	}


	// test AbstractAccount(float balance)
	@Test
	public void testSetBalanceInConstructor() {
		sut1 = new TestingAccount(1.0f);

		assertEquals(1.0f, sut1.getBalance(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeBalanceInConstructorThrowsException() {
		sut1 = new TestingAccount(-1);
	}


	// test AbstractAccount(AbstractAccount abstractAccount)
	@Test
	public void testCopyConstructor() {
		sut1 = new TestingAccount();
		TestingAccount sut2 = new TestingAccount(sut1);

		assertEquals(sut1.getId(), sut2.getId());
		assertEquals(sut1.getBalance(), sut2.getBalance(), 0);
		assertEquals(sut1.getAccountNumber(), sut2.getAccountNumber());
	}


	// test deposit(float balance)
	@Test
	public void testDepositIncreaseBalance() {
		sut1 = new TestingAccount(1.0f);
		sut1.deposit(2.0f);

		assertEquals(3.0f, sut1.getBalance(), 0.00001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositNegativeAmountThrowsException() {
		sut1 = new TestingAccount();
		sut1.deposit(-1.0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositZeroAmountThrowsException() {
		sut1 = new TestingAccount();
		sut1.deposit(0.0f);
	}


	// test decimalValue()
	@Ignore
	@Test
	public void testDecimalValue() {
		sut1 = new TestingAccount(1.1f);

		float balance = sut1.getBalance();

		final String EXPECTED_STRING = "The rounded account balance is " + Math.round(balance);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut1.decimalValue();
		final String decimalValueOutput = byteArrayOutputStream.toString();

		assertEquals("decimalValue() method does not produce the expected output",
				EXPECTED_STRING, decimalValueOutput);
	}


	// test printReport()
	@Test
	public void testPrintReport() {
		sut1 = new TestingAccount(1.0f);
		final int ID = sut1.getId();
		final int ACCOUNT_NUMBER = sut1.getAccountNumber();
		final float BALANCE = sut1.getBalance();

		final String EXPECTED_STRING = "ID = " + ID + ", account number = " + ACCOUNT_NUMBER
				+ ", balance = " + BALANCE;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut1.printReport();
		final String printReportOutput = byteArrayOutputStream.toString();

		assertEquals("printReport() method does not produce the expected output",
				EXPECTED_STRING, printReportOutput);
	}


	// test equalsContract
	@Test
	public void equalsContract() {
		EqualsVerifier.forClass(TestingAccount.class)
				.suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
				.verify();
	}

	private class TestingAccount extends AbstractAccount {
		public TestingAccount(float balance) {
			super(balance);
		}

		public TestingAccount() {
			super();
		}

		public TestingAccount(TestingAccount testingAccount) {
			super(testingAccount);
		}

		@Override
		public void withdraw(float amount) throws NotEnoughFundsException {

		}

		@Override
		public String getAccountType() {
			return null;
		}

		@Override
		public void parseFeed(Map<String, String> feed) {
		}
	}

}