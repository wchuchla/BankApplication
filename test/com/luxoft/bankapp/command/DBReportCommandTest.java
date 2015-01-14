package com.luxoft.bankapp.command;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.model.info.BankInfoCalc;
import com.luxoft.bankapp.service.BankCommander;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.newSecondCheckingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newSecondClient;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.newSavingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.newSecondSavingAccount;
import static org.junit.Assert.assertEquals;

public class DBReportCommandTest {

	private static final String MENU = "What info do you want: \r\n" +
			"0) Bank name\r\n" +
			"1) Number of clients\r\n" +
			"2) Number of accounts\r\n" +
			"3) List of clients\r\n" +
			"4) Sum of deposits\r\n" +
			"5) Sum of credits\r\n" +
			"6) List of clients by city\r\n";
	private static final String INVALID_OPERATION = "You entered invalid command number. Please try again.\r\n";
	private static final String INVALID_CONFIRMATION = "You entered wrong value. Please try again.\r\n";
	private static final String ANOTHER_OPERATION = "\r\nDo you want get other bank info? (Yes/yes/No/no)\r\n";
	private DBReportCommand sut;
	private Bank testBank;
	private Client testClient;
	private Client testClient2;

	@Before
	public void setUp() {
		testBank = newBank();
		testClient = newClient();
		testClient2 = newSecondClient();
		SavingAccount testSavingAccount = newSavingAccount();
		CheckingAccount testCheckingAccount = newCheckingAccount();
		SavingAccount testSavingAccount2 = newSecondSavingAccount();
		CheckingAccount testCheckingAccount2 = newSecondCheckingAccount();
		testClient.addAccount(testSavingAccount);
		testClient.addAccount(testCheckingAccount);
		testClient2.addAccount(testSavingAccount2);
		testClient2.addAccount(testCheckingAccount2);
		testBank.addClient(testClient);
		testBank.addClient(testClient2);

		BankCommander.activeBank = testBank;

		sut = new DBReportCommand();
	}

	// test execute()
	@Test
	public void testBankNameInExecute() {
		final String EXPECTED_STRING = MENU + "Bank name: " + testBank.getName() + ANOTHER_OPERATION;

		String input = "0" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

	@Test
	public void testNumberOfClientsInExecute() {
		int numberOfClients = BankInfoCalc.calcNumberOfClients(testBank);
		final String EXPECTED_STRING = MENU + "Number of clients: " + numberOfClients + ANOTHER_OPERATION;

		String input = "1" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

	@Test
	public void testNumberOfAccountsInExecute() {
		int numberOfAccounts = BankInfoCalc.calcNumberOfAccounts(testBank);
		final String EXPECTED_STRING = MENU + "Number of accounts: " + numberOfAccounts
				+ ANOTHER_OPERATION;

		String input = "2" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

	@Test
	public void testListOfClientsInExecute() {
		final String EXPECTED_STRING = MENU + "List of clients: \r\n" + testClient.getName() + "\r\n"
				+ testClient2.getName() + ANOTHER_OPERATION;

		String input = "3" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

	@Test
	public void testSumOfDepositInExecute() {
		float sumOfDeposit = BankInfoCalc.calcDepositSum(testBank);
		final String EXPECTED_STRING = MENU + "Sum of deposits: " + sumOfDeposit
				+ ANOTHER_OPERATION;

		String input = "4" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

	@Test
	public void testSumOfCreditInExecute() {
		float sumOfCredit = BankInfoCalc.calcCreditSum(testBank);
		final String EXPECTED_STRING = MENU + "Sum of credits: " + sumOfCredit
				+ ANOTHER_OPERATION;

		String input = "5" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

	@Test
	public void testListOfClientsByCityInExecute() {
		final String EXPECTED_STRING = MENU + "List of clients by city: \r\n" + testClient.getCity() + ": " + testClient
				.getName() + ", \r\n" + testClient2.getCity() + ": " + testClient2.getName() + ", " + ANOTHER_OPERATION;

		String input = "6" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

	// test execute()
	@Test
	public void testBankNameInExecuteWithTypos() {
		final String EXPECTED_STRING = MENU + INVALID_OPERATION + "Bank name: " + testBank.getName()
				+ ANOTHER_OPERATION + "n\r\n" + INVALID_CONFIRMATION;

		String input = "9" + "\r\n" + "0" + "\r\n" + "n" + "\r\n" + "no" + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.execute();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("execute() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}


	// test printCommandInfo()
	@Test
	public void testPrintCommandInfo() {
		final String EXPECTED_STRING = "Print bank info";

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.printCommandInfo();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("printCommandInfo() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}
}