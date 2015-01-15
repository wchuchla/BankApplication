package com.luxoft.bankapp.command;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.BankCommander;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.*;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;

public class GetAccountsCommandTest {

    private GetAccountsCommand sut;

    @Before
    public void setUp() {
        Bank testBank = newBank();
        Client testClient = newClient();
        SavingAccount testSavingAccount = newSavingAccount();
        CheckingAccount testCheckingAccount = newCheckingAccount();
        testClient.addAccount(testSavingAccount);
        testClient.addAccount(testCheckingAccount);
        testBank.addClient(testClient);

        BankCommander.activeBank = testBank;
        BankCommander.activeClient = testClient;

        sut = new GetAccountsCommand();
    }


    // test execute()
    @Test
    public void testGetAccountsInExecute() {
        final String expectedString = "List of accounts: \r\n" +
                "0) Account type = Saving account, ID = 0, account number = " + SAVING_ACCOUNT_ACCOUNT_NUMBER + ", " +
                "balance = " + SAVING_ACCOUNT_INITIAL_BALANCE + "\r\n" +
                "1) Account type = Checking account, ID = 0, account number = " + CHECKING_ACCOUNT_ACCOUNT_NUMBER +
                ", balance = " + CHECKING_ACCOUNT_INITIAL_BALANCE + ", overdraft = " + CHECKING_ACCOUNT_OVERDRAFT + "\r\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));

        sut.execute();
        final String executeOutput = byteArrayOutputStream.toString();

        assertEquals("printCommandInfo() method does not produce the expected output",
                expectedString, executeOutput);
    }

    // test printCommandInfo()
    @Test
    public void testPrintCommandInfo() {
        final String expectedString = "Get list of account";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printCommandInfo();
        final String printCommandInfoOutput = byteArrayOutputStream.toString();

        assertEquals("printCommandInfo() method does not produce the expected output",
                expectedString, printCommandInfoOutput);
    }

}