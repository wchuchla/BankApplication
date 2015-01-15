package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.BankDAO;
import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.ClientExistsException;
import com.luxoft.bankapp.exception.daoexception.BankNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.BankCommander;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.newSavingAccount;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DBSelectBankCommandTest {

    private DBSelectBankCommand sut;

    @Mock
    private BankDAO bankDAOMock;

    @Before
    public void setUp() throws DAOException {
        List<Client> testClients = new ArrayList<>();
        testClients.add(newClient());
        List<Account> testAccounts = new ArrayList<>();
        testAccounts.add(newSavingAccount());
        BankCommander.activeBank = null;

        sut = new DBSelectBankCommand(bankDAOMock);
    }

    @After
    public void tearDown() {
        System.setIn(System.in);
        BankCommander.activeBank = null;
    }


    // test execute()
    @Test
    public void testSelectBankInExecute() throws DAOException, ClientExistsException, AccountExistsException {
        when(bankDAOMock.getBankByName("testBank")).thenReturn(newBank());

        String input = "testBank" + "\r\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertNotNull(BankCommander.activeBank);
    }

    @Test
    public void testSelectNotExistingBankInExecute() throws DAOException, ClientExistsException,
            AccountExistsException {
        when(bankDAOMock.getBankByName("notExistingBank")).thenThrow(BankNotFoundException.class);

        String input = "notExistingBank" + "\r\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sut.execute();

        assertNull(BankCommander.activeBank);
    }


    // test printCommandInfo()
    @Test
    public void testPrintCommandInfo() {
        final String expectedString = "Select the active bank";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printCommandInfo();
        final String printCommandInfoOutput = byteArrayOutputStream.toString();

        assertEquals("printCommandInfo() method does not produce the expected output",
                expectedString, printCommandInfoOutput);
    }

}