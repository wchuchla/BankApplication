package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.ClientNotExistsException;
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
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newClient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class DBRemoveClientCommandTest {

    private DBRemoveClientCommand sut;
    private Bank testBank;

    @Mock
    private BankService bankServiceMock;

    @Mock
    private ClientDAO clientDAOMock;

    @Before
    public void setUp() {
        testBank = newBank();
        Client testClient = newClient();
        testBank.addClient(testClient);

        BankCommander.activeBank = testBank;
        BankCommander.activeClient = testClient;

        sut = new DBRemoveClientCommand(bankServiceMock, clientDAOMock);
    }

    @After
    public void tearDown() {
        System.setIn(System.in);
    }

    // test execute()
    @Test
    public void testRemoveClientInExecute() throws ClientNotExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Bank bank = (Bank) args[0];
            String clientName = (String) args[1];
            Client client = bank.getClients().get(clientName);
            bank.removeClient(client);
            return null;
        }).when(bankServiceMock).removeClient(any(Bank.class), any(String.class));

        String input = "Yes" + "\r\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertTrue(testBank.getClients().size() == 0);
    }

    @Test
    public void testRemoveNotExistingClientInExecute() throws ClientNotExistsException {
        doThrow(ClientNotExistsException.class).when(bankServiceMock).removeClient(any(Bank.class), any(String.class));

        BankCommander.activeClient = newClient();

        String input = "Yes" + "\r\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertTrue(testBank.getClients().size() == 1);
    }

    @Test
    public void testRemoveClientInExecuteWithTypos() throws ClientNotExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Bank bank = (Bank) args[0];
            String clientName = (String) args[1];
            Client client = bank.getClients().get(clientName);
            bank.removeClient(client);
            return null;
        }).when(bankServiceMock).removeClient(any(Bank.class), any(String.class));

        String input = "Ye" + "\r\n" + "Yes" + "\r\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertTrue(testBank.getClients().size() == 0);
    }

    // test printCommandInfo()
    @Test
    public void testPrintCommandInfo() {
        final String EXPECTED_STRING = "Remove the active client";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printCommandInfo();
        final String printCommandInfoOutput = byteArrayOutputStream.toString();

        assertEquals("printCommandInfo() method does not produce the expected output",
                EXPECTED_STRING, printCommandInfoOutput);
    }
}