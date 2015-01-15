package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.AccountDAO;
import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.NotEnoughFundsException;
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
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.*;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferCommandTest {

    private TransferCommand sut;

    private SavingAccount testSavingAccount;
    private SavingAccount testSavingAccount2;

    @Mock
    private BankService bankServiceMock;

    @Mock
    private ClientDAO clientDAOMock;

    @Mock
    private AccountDAO accountDAOMock;

    @Before
    public void setUp() throws DAOException, NotEnoughFundsException, AccountExistsException {
        Bank testBank = newBank();
        Client testClient = newClient();
        Client testClient2 = newSecondClient();
        testSavingAccount = newSavingAccount();
        testSavingAccount2 = newSecondSavingAccount();
        testClient.addAccount(testSavingAccount);
        testClient2.addAccount(testSavingAccount2);
        testClient2.setActiveAccount(testSavingAccount2);
        testBank.addClient(testClient);
        testBank.addClient(testClient2);

        BankCommander.activeBank = testBank;
        BankCommander.activeClient = testClient;

        sut = new TransferCommand(bankServiceMock, clientDAOMock, accountDAOMock);

        doNothing().when(accountDAOMock).save(any(Client.class), any(Account.class));
        when(clientDAOMock.findClientByName(testBank, CLIENT_SECOND_CLIENT_NAME)).thenReturn(testClient2);

        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            SavingAccount savingAccount = (SavingAccount) args[1];
            client.setActiveAccount(savingAccount);
            return null;
        }).when(bankServiceMock).setActiveAccount(testClient, testSavingAccount);

        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            SavingAccount savingAccount1 = (SavingAccount) args[0];
            SavingAccount savingAccount2 = (SavingAccount) args[1];
            float amount = (float) args[2];
            savingAccount1.withdraw(amount);
            savingAccount2.deposit(amount);
            return null;
        }).when(bankServiceMock).transfer(testSavingAccount, testSavingAccount2, 1.0f);

    }

    @After
    public void tearDown() {
        System.setIn(System.in);
    }


    // test execute()
    @Test
    public void testWithdrawInExecute() {
        float amount = 1.0f;

        String input = "0" + "\r\n" + amount + "\r\n" + CLIENT_SECOND_CLIENT_NAME + "\r\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE - amount, testSavingAccount.getBalance(), 0.0f);
        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE + amount, testSavingAccount2.getBalance(), 0.0f);
    }

    @Test
    public void testWithdrawNotEnoughFundInExecute() throws NotEnoughFundsException {
        doThrow(NotEnoughFundsException.class).when(bankServiceMock).transfer(testSavingAccount, testSavingAccount2,
                101.0f);

        float amount = SAVING_ACCOUNT_INITIAL_BALANCE + 1.0f;

        String input = "0" + "\r\n" + amount + "\r\n" + CLIENT_SECOND_CLIENT_NAME + "\r\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE, testSavingAccount.getBalance(), 0.0f);
        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE, testSavingAccount2.getBalance(), 0.0f);
    }

    @Test
    public void testWithdrawInExecuteWithTypos() throws DAOException, NotEnoughFundsException {
        float amount = 1.0f;
        float negativeAmount = -1.0f;

        String input = "5" + "\r\n" + "0" + "\r\n" + negativeAmount + "\r\n" + amount + "\r\n" + "testClient1" + "\r\n"
                + CLIENT_SECOND_CLIENT_NAME + "\r\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE - amount, testSavingAccount.getBalance(), 0.0f);
        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE + amount, testSavingAccount2.getBalance(), 0.0f);
    }


    // test printCommandInfo()
    @Test
    public void testPrintCommandInfo() {
        final String EXPECTED_STRING = "Make a transfer";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printCommandInfo();
        final String printCommandInfoOutput = byteArrayOutputStream.toString();

        assertEquals("printCommandInfo() method does not produce the expected output",
                EXPECTED_STRING, printCommandInfoOutput);
    }

}