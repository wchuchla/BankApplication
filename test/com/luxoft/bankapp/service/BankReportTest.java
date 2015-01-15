package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.newSavingAccount;
import static org.junit.Assert.assertEquals;

public class BankReportTest {

    private static Bank testBank;

    @BeforeClass
    public static void setUp() {
        testBank = newBank();
        Client testClient = newClient();
        SavingAccount testSavingAccount = newSavingAccount();
        CheckingAccount testCheckingAccount = newCheckingAccount();

        testClient.addAccount(testSavingAccount);
        testClient.addAccount(testCheckingAccount);
        testBank.addClient(testClient);
    }


    // test getNumberOfClients(Bank bank)
    @Test
    public void testGetNumberOfClients() throws Exception {
        final String expectedString = "The number of bank clients = 1\r\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        BankReport.getNumberOfClients(testBank);
        final String getNumberOfClientsOutput = byteArrayOutputStream.toString();

        assertEquals("getNumberOfClients(Bank bank) method does not produce the expected output",
                expectedString, getNumberOfClientsOutput);
    }


    // test getAccountsNumber(Bank bank)
    @Test
    public void testGetAccountsNumber() throws Exception {
        final String expectedString = "The total number of accounts for all bank clients = 2\r\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        BankReport.getAccountsNumber(testBank);
        final String getAccountsNumberOutput = byteArrayOutputStream.toString();

        assertEquals("getAccountsNumber() method does not produce the expected output",
                expectedString, getAccountsNumberOutput);
    }


    // test GetClientsSorted()
    @Test
    public void testGetClientsSorted() throws Exception {
        final String expectedString = "List of all accounts sorted by balance: \r\nAccount type = Saving account, ID" +
                " = 0, account number = 10000000, balance = 100.0\r\nAccount type = Checking account, ID = 0, account " +
                "number = 10000002, balance = 100.0, overdraft = 1000.0\r\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        BankReport.getClientsSorted(testBank);
        final String getAccountsNumberOutput = byteArrayOutputStream.toString();

        assertEquals("getClientsSorted() method does not produce the expected output",
                expectedString, getAccountsNumberOutput);
    }

    @Test
    public void testGetBankCreditSum() throws Exception {
        final String expectedString = "Total amount of credits granted to the bank clients = 0.0\r\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        BankReport.getBankCreditSum(testBank);
        final String getBankCreditSumOutput = byteArrayOutputStream.toString();

        assertEquals("getBankCreditSum() method does not produce the expected output",
                expectedString, getBankCreditSumOutput);
    }

    @Test
    public void testGetClientsByCity() throws Exception {
        final String expectedString = "List of clients by cities: \r\nFirst City: First Client, \r\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        BankReport.getClientsByCity(testBank);
        final String getClientsByCityOutput = byteArrayOutputStream.toString();

        assertEquals("getClientsByCity() method does not produce the expected output",
                expectedString, getClientsByCityOutput);
    }
}