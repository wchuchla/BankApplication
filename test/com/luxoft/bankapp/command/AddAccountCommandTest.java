package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.AccountDAO;
import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.CheckingAccount;
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

import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.newSavingAccount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddAccountCommandTest {

    private AddAccountCommand sut;

    private Client testClient;

    @Mock
    private BankService bankServiceMock;
    @Mock
    private AccountDAO accountDAOMock;

    @Before
    public void setUp() throws DAOException {
        testClient = newClient();

        BankCommander.activeClient = testClient;

        sut = new AddAccountCommand(bankServiceMock, accountDAOMock);

        doNothing().when(accountDAOMock).save(any(Client.class), any(Account.class));
    }

    @After
    public void tearDown() {
        System.setIn(System.in);
    }


    // test execute()
    @Test
    public void testAddSavingAccountInExecute() throws AccountExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            SavingAccount savingAccount = (SavingAccount) args[1];
            client.addAccount(savingAccount);
            return null;
        }).when(bankServiceMock).addAccount(any(Client.class), any(SavingAccount.class));

        String accountType = "Saving\r\n";

        System.setIn(new ByteArrayInputStream(accountType.getBytes()));

        sut.execute();

        assertTrue(testClient.getAccounts().size() == 1);
    }

    @Test
    public void testAddCheckingAccountInExecute() throws AccountExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            CheckingAccount checkingAccount = (CheckingAccount) args[1];
            client.addAccount(checkingAccount);
            return null;
        }).when(bankServiceMock).addAccount(any(Client.class), any(CheckingAccount.class));

        String accountType = "Checking\r\n";
        System.setIn(new ByteArrayInputStream(accountType.getBytes()));

        sut.execute();

        assertTrue(testClient.getAccounts().size() == 1);
    }

    @Test
    public void testAddExistingSavingAccountInExecute() throws AccountExistsException {
        doThrow(AccountExistsException.class).when(bankServiceMock).addAccount(any(Client.class), any(SavingAccount
                .class));

        testClient.addAccount(newSavingAccount());

        String accountType = "Saving\r\n";

        System.setIn(new ByteArrayInputStream(accountType.getBytes()));

        sut.execute();

        assertTrue(testClient.getAccounts().size() == 1);
    }


    @Test
    public void testAddExistingCheckingAccountInExecute() throws AccountExistsException {
        doThrow(AccountExistsException.class).when(bankServiceMock).addAccount(any(Client.class), any(CheckingAccount
                .class));

        testClient.addAccount(newCheckingAccount());

        String accountType = "Checking\r\n";
        System.setIn(new ByteArrayInputStream(accountType.getBytes()));

        sut.execute();

        assertTrue(testClient.getAccounts().size() == 1);
    }

    @Test
    public void testAddAccountWithInvalidAccountTypeInExecute() throws AccountExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            SavingAccount savingAccount = (SavingAccount) args[1];
            client.addAccount(savingAccount);
            return null;
        }).when(bankServiceMock).addAccount(any(Client.class), any(SavingAccount.class));

        String accountType = "Savin\r\nSaving\n";
        System.setIn(new ByteArrayInputStream(accountType.getBytes()));

        sut.execute();

        assertTrue(testClient.getAccounts().size() == 1);
    }

    // test printCommandInfo()
    @Test
    public void testPrintCommandInfo() {
        final String EXPECTED_STRING = "Create new account";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printCommandInfo();
        final String printCommandInfoOutput = byteArrayOutputStream.toString();

        assertEquals("printCommandInfo() method does not produce the expected output",
                EXPECTED_STRING, printCommandInfoOutput);
    }

}