package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.daoexception.ClientNotFoundException;
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
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DBSelectClientCommandTest {

	private DBSelectClientCommand sut;
	private Bank testBank;
	private Client testClient;

	@Mock
	private BankService bankServiceMock;

	@Mock
	private ClientDAO clientDAOMock;

	@Before
	public void setUp() throws DAOException {
		testBank = newBank();
		testClient = newClient();
		testBank.addClient(testClient);

		BankCommander.activeBank = testBank;
		BankCommander.activeClient = null;

		sut = new DBSelectClientCommand(clientDAOMock);

		doNothing().when(clientDAOMock).remove(any(Bank.class), any(Client.class));
	}

	@After
	public void tearDown() {
		System.setIn(System.in);
	}


	// test execute()
	@Test
	public void testSelectClientInExecute() throws DAOException {
		when(clientDAOMock.findClientByName(testBank, CLIENT_NAME)).thenReturn(testClient);

		String input = CLIENT_NAME + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertTrue(BankCommander.activeClient == testClient);
	}

	@Test
	public void testSelectNotExistingClientInExecute() throws DAOException {
		when(clientDAOMock.findClientByName(testBank, CLIENT_INVALID_NAME)).thenThrow(ClientNotFoundException.class);

		String input = CLIENT_INVALID_NAME + "\r\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		sut.execute();

		assertNull(BankCommander.activeClient);
	}

	// test printCommandInfo()
	@Test
	public void testPrintCommandInfo() {
		final String EXPECTED_STRING = "Select the active client";

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));
		sut.printCommandInfo();
		final String printCommandInfoOutput = byteArrayOutputStream.toString();

		assertEquals("printCommandInfo() method does not produce the expected output",
				EXPECTED_STRING, printCommandInfoOutput);
	}

}