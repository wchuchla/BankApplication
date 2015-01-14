package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.AccountDAO;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.BankCommander;
import com.luxoft.bankapp.service.BankService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.SAVING_ACCOUNT_INITIAL_BALANCE;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.newSavingAccount;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class WithdrawCommandTest {

	private WithdrawCommand sut;

	private Client testClient;
	private SavingAccount testSavingAccount;

	@Mock
	private BankService bankServiceMock;

	@Mock
	private AccountDAO accountDAOMock;

	@Before
	public void setUp() throws DAOException {
		Bank testBank = newBank();
		testClient = newClient();
		testSavingAccount = newSavingAccount();
		testClient.addAccount(testSavingAccount);
		testBank.addClient(testClient);

		BankCommander.activeBank = testBank;
		BankCommander.activeClient = testClient;

		sut = new WithdrawCommand(bankServiceMock, accountDAOMock);

		doNothing().when(accountDAOMock).save(any(Client.class), any(Account.class));
	}

	@After
	public void tearDown() {
		System.setIn(System.in);
	}


	// test execute()
	@Test
	public void testWithdrawInExecute() {
		doAnswer(invocationOnMock -> {
			Object[] args = invocationOnMock.getArguments();
			Client client = (Client) args[0];
			SavingAccount savingAccount = (SavingAccount) args[1];
			client.setActiveAccount(savingAccount);
			return null;
		}).when(bankServiceMock).setActiveAccount(testClient, testSavingAccount);

		float amount = 1;

		String input = "0" + "\r\n" + amount + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE - amount, testSavingAccount.getBalance(), 0.0f);
	}

	@Test
	public void testWithdrawNotEnoughFundInExecute() {
		doAnswer(invocationOnMock -> {
			Object[] args = invocationOnMock.getArguments();
			Client client = (Client) args[0];
			SavingAccount savingAccount = (SavingAccount) args[1];
			client.setActiveAccount(savingAccount);
			return null;
		}).when(bankServiceMock).setActiveAccount(testClient, testSavingAccount);

		float amount = SAVING_ACCOUNT_INITIAL_BALANCE + 1;

		String input = "0" + "\r\n" + amount + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE, testSavingAccount.getBalance(), 0.0f);
	}

	@Test
	public void testDepositInExecuteWithTypos() {

		doAnswer(invocationOnMock -> {
			Object[] args = invocationOnMock.getArguments();
			Client client = (Client) args[0];
			SavingAccount savingAccount = (SavingAccount) args[1];
			client.setActiveAccount(savingAccount);
			return null;
		}).when(bankServiceMock).setActiveAccount(testClient, testSavingAccount);

		float amount = 1;
		float negativeAmount = -1;

		String input = "5" + "\r\n" + "0" + "\r\n" + negativeAmount + "\r\n" + amount + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE - amount, testSavingAccount.getBalance(), 0.0f);
	}


	// test printCommandInfo()
	@Test
	public void testPrintCommandInfo() {
		final String EXPECTED_STRING = "Make a withdraw";

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.printCommandInfo();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("printCommandInfo() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

}