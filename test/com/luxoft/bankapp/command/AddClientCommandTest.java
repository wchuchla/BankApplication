package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.ClientExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
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
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddClientCommandTest {

	@Mock
	private static BankService bankServiceMock;
	@Mock
	private static ClientDAO clientDAOMock;
	private AddClientCommand sut;
	private Bank testBank;

	@Before
	public void setUp() throws DAOException {
		testBank = newBank();

		BankCommander.activeBank = testBank;

		sut = new AddClientCommand(bankServiceMock, clientDAOMock);

		doNothing().when(clientDAOMock).save(any(Bank.class), any(Client.class));
	}

	@After
	public void tearDown() {
		System.setIn(System.in);
	}

	// test execute()
	@Test
	public void testAddClientInExecute() throws ClientExistsException {
		doAnswer(invocationOnMock -> {
			Object[] args = invocationOnMock.getArguments();
			Bank bank = (Bank) args[0];
			Client client = (Client) args[1];
			bank.addClient(client);
			return null;
		}).when(bankServiceMock).addClient(any(Bank.class), any(Client.class));

		String input = CLIENT_NAME + "\r\n" + CLIENT_GENDER + "\r\n" + CLIENT_EMAIL + "\r\n" + CLIENT_PHONE_NUMBER
				+ "\r\n" + CLIENT_CITY + "\r\n" + CLIENT_INITIAL_OVERDRAFT + "\r\n";

		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertTrue(testBank.getClients().size() == 1);
		assertClient(testBank.getClients().get(CLIENT_NAME));
	}

	@Test
	public void testAddExistingClientInExecute() throws ClientExistsException {
		doThrow(ClientExistsException.class).when(bankServiceMock).addClient(any(Bank.class), any(Client.class));

		testBank.addClient(newClient());

		String input = CLIENT_NAME + "\r\n" + CLIENT_GENDER + "\r\n" + CLIENT_EMAIL + "\r\n" + CLIENT_PHONE_NUMBER
				+ "\r\n" + CLIENT_CITY + "\r\n" + CLIENT_INITIAL_OVERDRAFT + "\r\n";

		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertTrue(testBank.getClients().size() == 1);
	}

	@Test
	public void testAddClientInExecuteWithTypos() throws ClientExistsException {
		doAnswer(invocationOnMock -> {
			Object[] args = invocationOnMock.getArguments();
			Bank bank = (Bank) args[0];
			Client client = (Client) args[1];
			bank.addClient(client);
			return null;
		}).when(bankServiceMock).addClient(any(Bank.class), any(Client.class));

		String input = "testClient" + "\r\n" + CLIENT_NAME + "\r\n" + "MAL" + "\r\n" + CLIENT_GENDER + "\r\n"
				+ "testclientgmail.com" + "\r\n" + CLIENT_EMAIL + "\r\n" + "0124568" + "\r\n" + CLIENT_PHONE_NUMBER
				+ "\r\n" + "newYr23ok" + "\r\n" + CLIENT_CITY + "\r\n" + "-1000" + "\r\n" + CLIENT_INITIAL_OVERDRAFT
				+ "\r\n";

		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertTrue(testBank.getClients().size() == 1);
		assertClient(testBank.getClients().get(CLIENT_NAME));
	}


	// test printCommandInfo()
	@Test
	public void testPrintCommandInfo() {
		final String EXPECTED_STRING = "Register new client";

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.printCommandInfo();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("printCommandInfo() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}
}