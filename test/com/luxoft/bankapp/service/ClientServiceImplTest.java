package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.*;
import com.luxoft.bankapp.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.*;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.*;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {

    public static final String SAVING = "Saving";
    public static final String CHECKING = "Checking";
    private ClientService sut;
    private Bank testBank;
    private Client testClient1;
    private Client testClient2;
    private SavingAccount testSavingAccount;
    private CheckingAccount testCheckingAccount;
    private SavingAccount testSavingAccount2;
    private CheckingAccount testCheckingAccount2;

    @Mock
    private BankService bankServiceMock;


    @Before
    public void setUp() {
        testBank = newBank();
        testClient1 = newClient();
        testClient2 = newSecondClient();
        testSavingAccount = newSavingAccount();
        testSavingAccount2 = newSecondSavingAccount();
        testCheckingAccount = newCheckingAccount();
        testCheckingAccount2 = newSecondCheckingAccount();
        testClient1.addAccount(testSavingAccount);
        testClient1.addAccount(testCheckingAccount);
        testBank.addClient(testClient1);
        sut = new ClientServiceImpl(testBank, bankServiceMock);
    }


    // test getAccount(Client client, String type)
    @Test
    public void testGetSavingAccountInGetAccount() throws AccountNotExistsException {
        when(bankServiceMock.getAccountByType(testClient1, SAVING)).thenReturn(testSavingAccount);

        Account gootenAccount = sut.getAccount(testClient1, SAVING);

        assertTrue(testSavingAccount == gootenAccount);
    }

    @Test
    public void testGetCheckingAccountInGetAccount() throws AccountNotExistsException {
        when(bankServiceMock.getAccountByType(testClient1, CHECKING)).thenReturn(testCheckingAccount);

        Account gootenAccount = sut.getAccount(testClient1, CHECKING);

        assertTrue(testCheckingAccount == gootenAccount);
    }

    @Test(expected = AccountNotExistsException.class)
    public void testGetNotExistingSavingAccountInGetAccount() throws AccountNotExistsException {
        when(bankServiceMock.getAccountByType(testClient2, SAVING)).thenThrow(AccountNotExistsException.class);

        sut.getAccount(testClient2, SAVING);
    }

    @Test(expected = AccountNotExistsException.class)
    public void testGetNotExistingCheckingAccountInGetAccount() throws AccountNotExistsException {
        when(bankServiceMock.getAccountByType(testClient2, CHECKING)).thenThrow(AccountNotExistsException.class);

        sut.getAccount(testClient2, CHECKING);
    }


    // test getClient(String name)
    @Test
    public void testGetClientInGetClient() throws ClientNotExistsException {
        when(bankServiceMock.getClient(testBank, CLIENT_NAME)).thenReturn(testClient1);

        Client gottenClient = sut.getClient(CLIENT_NAME);

        assertTrue(testClient1 == gottenClient);
    }

    @Test(expected = ClientNotExistsException.class)
    public void testGetNotExistingClientInGetClientThrowsException() throws ClientNotExistsException {
        when(bankServiceMock.getClient(testBank, CLIENT_INVALID_NAME)).thenThrow(ClientNotExistsException.class);

        sut.getClient(CLIENT_INVALID_NAME);
    }


    // test getBalance(Account account)
    @Test
    public void testGetSavingAccountBalanceInGetBalance() {
        float gottenBalance = sut.getBalance(testSavingAccount);

        assertEquals(testSavingAccount.getBalance(), gottenBalance, 0.0f);
    }

    @Test
    public void testGetCheckingAccountBalanceInGetBalance() {
        float gottenBalance = sut.getBalance(testCheckingAccount);

        assertEquals(testCheckingAccount.getBalance(), gottenBalance, 0.0f);
    }


    // test deposit(Account account, float amount)
    @Test
    public void testSavingAccountDepositInDeposit() {
        float amount = 100;

        sut.deposit(testSavingAccount, amount);

        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE + amount, testSavingAccount.getBalance(), 0.0f);
    }

    @Test
    public void testCheckingAccountDepositInDeposit() {
        float amount = 100;

        sut.deposit(testCheckingAccount, amount);

        assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE + amount, testCheckingAccount.getBalance(), 0.0f);
    }


    // test withdraw(Account account, float amount)
    @Test
    public void testSavingAccountWithdrawInWithdraw() throws NotEnoughFundsException {
        float amount = SAVING_ACCOUNT_INITIAL_BALANCE;

        sut.withdraw(testSavingAccount, amount);

        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE - amount, testSavingAccount.getBalance(), 0.0f);
    }

    @Test
    public void testCheckingAccountWithdrawInWithdraw() throws NotEnoughFundsException {
        float amount = CHECKING_ACCOUNT_INITIAL_BALANCE;

        sut.withdraw(testCheckingAccount, amount);

        assertEquals(CHECKING_ACCOUNT_INITIAL_BALANCE - amount, testCheckingAccount.getBalance(), 0.0f);
    }

    @Test(expected = NotEnoughFundsException.class)
    public void testNotEnoughFundsSavingAccountWithdrawInWithdraw() throws NotEnoughFundsException {
        float amount = SAVING_ACCOUNT_INITIAL_BALANCE + 100;
        sut.withdraw(testSavingAccount, amount);
    }

    @Test(expected = NotEnoughFundsException.class)
    public void testNotEnoughFundsCheckingAccountWithdrawInWithdraw() throws NotEnoughFundsException {
        float amount = CHECKING_ACCOUNT_INITIAL_BALANCE + CHECKING_ACCOUNT_OVERDRAFT + 100;
        sut.withdraw(testCheckingAccount, amount);
    }


    // test addClient(Bank bank, Client client)
    @Test
    public void testAddClientInAddClient() throws ClientExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Bank bank = (Bank) args[0];
            Client client = (Client) args[1];
            bank.addClient(client);
            return null;
        }).when(bankServiceMock).addClient(testBank, testClient2);

        sut.addClient(testBank, testClient2);

        assertTrue(testBank.getClients().containsValue(testClient2));
    }

    @Test(expected = ClientExistsException.class)
    public void testAddExistsClientInAddClientThrowsException() throws ClientExistsException {
        doThrow(ClientExistsException.class).when(bankServiceMock).addClient(testBank, testClient1);

        sut.addClient(testBank, testClient1);
    }


    // test removeClient(Bank bank, String name)
    @Test
    public void testRemoveClientInRemoveClient() throws ClientNotExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Bank bank = (Bank) args[0];
            String clientName = (String) args[1];
            Client client = bank.getClients().get(clientName);
            bank.removeClient(client);
            return null;
        }).when(bankServiceMock).removeClient(testBank, CLIENT_NAME);

        sut.removeClient(testBank, CLIENT_NAME);

        assertFalse(testBank.getClients().containsValue(testClient1));
    }

    @Test(expected = ClientNotExistsException.class)
    public void testRemoveNotExistingClientInRemoveClientThrowsException() throws ClientNotExistsException {
        doThrow(ClientNotExistsException.class).when(bankServiceMock).removeClient(testBank, CLIENT_INVALID_NAME);

        sut.removeClient(testBank, CLIENT_INVALID_NAME);
    }


    // test addAccount(Client client, Account account)
    @Test
    public void testAddSavingAcountInAddAccount() throws AccountExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            SavingAccount savingAccount = (SavingAccount) args[1];
            client.addAccount(savingAccount);
            return null;
        }).when(bankServiceMock).addAccount(testClient2, testSavingAccount2);

        sut.addAccount(testClient2, testSavingAccount2);
        Set<Account> accounts = testClient2.getAccounts();

        assertTrue(accounts.contains(testSavingAccount2));
        assertTrue(accounts.size() == 1);
    }

    @Test
    public void testAddCheckingAcountInAddAccount() throws AccountExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            CheckingAccount checkingAccount = (CheckingAccount) args[1];
            client.addAccount(checkingAccount);
            return null;
        }).when(bankServiceMock).addAccount(testClient2, testCheckingAccount2);

        sut.addAccount(testClient2, testCheckingAccount2);
        Set<Account> accounts = testClient2.getAccounts();

        assertTrue(accounts.contains(testCheckingAccount2));
        assertTrue(accounts.size() == 1);
    }

    @Test
    public void testAddSavingAndCheckingAcountInAddAccount() throws AccountExistsException {
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            SavingAccount savingAccount = (SavingAccount) args[1];
            client.addAccount(savingAccount);
            return null;
        }).when(bankServiceMock).addAccount(testClient2, testSavingAccount2);

        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            Client client = (Client) args[0];
            CheckingAccount checkingAccount = (CheckingAccount) args[1];
            client.addAccount(checkingAccount);
            return null;
        }).when(bankServiceMock).addAccount(testClient2, testCheckingAccount2);

        sut.addAccount(testClient2, testSavingAccount2);
        sut.addAccount(testClient2, testCheckingAccount2);

        Set<Account> accounts = testClient2.getAccounts();

        assertTrue(accounts.contains(testSavingAccount2));
        assertTrue(accounts.contains(testCheckingAccount2));
        assertTrue(accounts.size() == 2);
    }

    @Test(expected = AccountExistsException.class)
    public void testAddExisitngSavingAccountInAddAccountThrowsException() throws AccountExistsException {
        doThrow(AccountExistsException.class).when(bankServiceMock).addAccount(testClient1, testSavingAccount);

        sut.addAccount(testClient1, testSavingAccount);
    }

    @Test(expected = AccountExistsException.class)
    public void testAddExisitngCheckingAccountInAddAccountThrowsException() throws AccountExistsException {
        doThrow(AccountExistsException.class).when(bankServiceMock).addAccount(testClient1, testCheckingAccount);

        sut.addAccount(testClient1, testCheckingAccount);
    }


    // test boolean isAccountNumberAvailable(Bank bank, int accountNumber)
    @Test
    public void testIsAccountNumberAvailableIfIsNotUsed() {
        when(bankServiceMock.isAccountNumberAvailable(testBank, 1)).thenReturn(true);

        assertTrue(sut.isAccountNumberAvailable(testBank, 1));
    }

    @Test
    public void testIsAccountNumberAvailableIfIsUsed() {
        when(bankServiceMock.isAccountNumberAvailable(testBank, SAVING_ACCOUNT_ACCOUNT_NUMBER)).thenReturn(false);

        assertFalse(sut.isAccountNumberAvailable(testBank, SAVING_ACCOUNT_ACCOUNT_NUMBER));
    }

}