package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.*;
import com.luxoft.bankapp.model.*;
import com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper;
import com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.CHECKING_ACCOUNT_OVERDRAFT;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.newSecondClient;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.newSavingAccount;
import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class BankServiceImplTest {

    private final BankService sut = new BankServiceImpl();
    private Bank testBank;
    private Client testClient1;
    private Client testClient2;
    private SavingAccount testSavingAccount;
    private CheckingAccount testCheckingAccount;

    @Before
    public void setUp() {
        testBank = newBank();
        testClient1 = newClient();
        testClient2 = newSecondClient();
        testSavingAccount = newSavingAccount();
        testCheckingAccount = newCheckingAccount();
    }


    // test void addClient(Bank bank, Client client)
    @Test
    public void testAddClientInAddClient() throws ClientExistsException {
        sut.addClient(testBank, testClient1);

        Map<String, Client> clients = testBank.getClients();
        Client addedClient = clients.get(testClient1.getName());

        assertTrue(testClient1 == addedClient);
        assertEquals(1, clients.size());
    }

    @Test
    public void testAddTwoClientsInAddClient() throws ClientExistsException {
        sut.addClient(testBank, testClient1);
        sut.addClient(testBank, testClient2);

        Map<String, Client> clients = testBank.getClients();
        Client addedFirstClient = clients.get(testClient1.getName());
        Client addedSecondClient = clients.get(testClient2.getName());

        assertTrue(testClient1 == addedFirstClient);
        assertTrue(testClient2 == addedSecondClient);
        assertEquals(2, clients.size());
    }


    @Test(expected = ClientExistsException.class)
    public void testAddExistsClientInAddClientThrowsException() throws ClientExistsException {
        sut.addClient(testBank, testClient1);
        sut.addClient(testBank, testClient1);
    }


    // test void removeClient(Bank bank, String name)
    @Test
    public void testRemoveClientInRemoveClient() throws ClientExistsException, ClientNotExistsException {
        sut.addClient(testBank, testClient1);

        sut.removeClient(testBank, testClient1.getName());

        Map<String, Client> clients = testBank.getClients();

        assertEquals(0, clients.size());
    }

    @Test(expected = ClientNotExistsException.class)
    public void testRemoveNotExistingClientInRemoveClientThrowsException() throws ClientNotExistsException {
        sut.removeClient(testBank, testClient1.getName());
    }


    // test addAccount(Client client, Account account)
    @Test
    public void testAddSavingAcountInAddAccount() throws AccountExistsException {
        sut.addAccount(testClient1, testSavingAccount);
        Set<Account> accounts = testClient1.getAccounts();

        assertTrue(accounts.contains(testSavingAccount));
        assertTrue(accounts.size() == 1);
    }

    @Test
    public void testAddCheckingAcountInAddAccount() throws AccountExistsException {
        sut.addAccount(testClient1, testCheckingAccount);
        Set<Account> accounts = testClient1.getAccounts();

        assertTrue(accounts.contains(testCheckingAccount));
        assertTrue(accounts.size() == 1);
    }

    @Test
    public void testAddSavingAndCheckingAcountInAddAccount() throws AccountExistsException {
        sut.addAccount(testClient1, testSavingAccount);
        sut.addAccount(testClient1, testCheckingAccount);
        Set<Account> accounts = testClient1.getAccounts();

        assertTrue(accounts.contains(testSavingAccount));
        assertTrue(accounts.contains(testCheckingAccount));
        assertTrue(accounts.size() == 2);
    }

    @Test(expected = AccountExistsException.class)
    public void testAddTwoSavingAcountsInAddAccountThrowsException() throws AccountExistsException {
        sut.addAccount(testClient1, testSavingAccount);
        sut.addAccount(testClient1, testSavingAccount);
    }

    @Test(expected = AccountExistsException.class)
    public void testAddTwoCheckingAcountsInAddAccountThrowsException() throws AccountExistsException {
        sut.addAccount(testClient1, testCheckingAccount);
        sut.addAccount(testClient1, testCheckingAccount);
    }


    // test setActiveAccount(Client client, Account account)
    @Test
    public void testSetSavingAccountAsActiveAccountInSetActiveAccount() {
        sut.setActiveAccount(testClient1, testSavingAccount);

        assertTrue(testClient1.getActiveAccount() == testSavingAccount);
    }

    @Test
    public void testSetCheckingAccountAsActiveAccountInSetActiveAccount() {
        sut.setActiveAccount(testClient1, testCheckingAccount);

        assertTrue(testClient1.getActiveAccount() == testCheckingAccount);
    }


    // test getClient(Bank bank, String name)
    @Test
    public void testGetClientInGetClient() throws ClientNotExistsException {
        testBank.addClient(testClient1);

        Client gottenClient = sut.getClient(testBank, testClient1.getName());

        assertTrue(testClient1 == gottenClient);
    }

    @Test(expected = ClientNotExistsException.class)
    public void testGetNotExistingClientInGetClientThrowsException() throws ClientNotExistsException {
        sut.getClient(testBank, testClient1.getName());
    }


    // test getAccountByType(Client client, String type)
    @Test
    public void testGetSavingAccountInGetAccountByType() throws AccountNotExistsException {
        testClient1.addAccount(testSavingAccount);

        Account gottenSavingAccount = sut.getAccountByType(testClient1, "Saving");

        assertTrue(testSavingAccount == gottenSavingAccount);
    }

    @Test
    public void testGetCheckingAccountInGetAccountByType() throws AccountNotExistsException {
        testClient1.addAccount(testCheckingAccount);

        Account gottenCheckingAccount = sut.getAccountByType(testClient1, "Checking");

        assertTrue(testCheckingAccount == gottenCheckingAccount);
    }

    @Test(expected = AccountNotExistsException.class)
    public void testGetNotExistingSavingAccountInGetAccountByType() throws AccountNotExistsException {
        sut.getAccountByType(testClient1, "Saving");
    }

    @Test(expected = AccountNotExistsException.class)
    public void testGetNotExistingCheckingAccountInGetAccountByType() throws AccountNotExistsException {
        sut.getAccountByType(testClient1, "Checking");
    }


    // test isAccountNumberAvailable(Bank bank, int accountNumber)
    @Test
    public void testIsAccountNumberAvailableIfIsNotUsed() {
        assertTrue(sut.isAccountNumberAvailable(testBank, SavingAccountEntityHelper.SAVING_ACCOUNT_ACCOUNT_NUMBER));
    }

    @Test
    public void testIsAccountNumberAvailableIfIsUsed() {
        testBank.addClient(testClient1);
        testClient1.addAccount(testSavingAccount);

        assertFalse(sut.isAccountNumberAvailable(testBank, SavingAccountEntityHelper.SAVING_ACCOUNT_ACCOUNT_NUMBER));
    }


    // test transfer(Account payerAccount, Account beneficiaryAccount, float amount)
    @Test
    public void testTransferFromSavingAccountInTransfer() throws NotEnoughFundsException {
        float amount = SavingAccountEntityHelper.SAVING_ACCOUNT_INITIAL_BALANCE;
        sut.transfer(testSavingAccount, testCheckingAccount, amount);

        assertEquals(SavingAccountEntityHelper.SAVING_ACCOUNT_INITIAL_BALANCE - amount, testSavingAccount.getBalance(), 0.0f);
        assertEquals(CheckingAccountEntityHelper.CHECKING_ACCOUNT_INITIAL_BALANCE + amount, testCheckingAccount.getBalance(), 0.0f);
    }

    @Test
    public void testTransferFromCheckingAccountInTransfer() throws NotEnoughFundsException {
        float amount = SavingAccountEntityHelper.SAVING_ACCOUNT_INITIAL_BALANCE;
        sut.transfer(testCheckingAccount, testSavingAccount, amount);

        assertEquals(SavingAccountEntityHelper.SAVING_ACCOUNT_INITIAL_BALANCE + amount, testSavingAccount.getBalance(), 0.0f);
        assertEquals(CheckingAccountEntityHelper.CHECKING_ACCOUNT_INITIAL_BALANCE - amount, testCheckingAccount.getBalance(), 0.0f);
    }

    @Test(expected = NotEnoughFundsException.class)
    public void testNotEnoughFundsTransferFromSavingAccountInTransfer() throws NotEnoughFundsException {
        float amount = SavingAccountEntityHelper.SAVING_ACCOUNT_INITIAL_BALANCE + 100;

        sut.transfer(testSavingAccount, testCheckingAccount, amount);
    }

    @Test(expected = NotEnoughFundsException.class)
    public void testNotEnoughFundsTransferFromCheckingAccountInTransfer() throws NotEnoughFundsException {
        float amount = CheckingAccountEntityHelper.CHECKING_ACCOUNT_INITIAL_BALANCE + CHECKING_ACCOUNT_OVERDRAFT + 100;

        sut.transfer(testCheckingAccount, testSavingAccount, amount);
    }


    // test saveClient(Client client, String filePath) and loadClient(String filePath)
    @Test
    public void testClientSerialization() {
        sut.saveClient(testClient1, "test/resources/saveTestClient");

        Client loadedClient = sut.loadClient("test/resources/saveTestClient");

        assertReflectionEquals(testClient1, loadedClient);
    }
}