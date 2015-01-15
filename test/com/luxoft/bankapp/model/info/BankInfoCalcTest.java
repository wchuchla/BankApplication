package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.BANK_NAME;
import static com.luxoft.bankapp.helper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.*;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.*;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankInfoCalcTest {

    private Bank testBank;
    private Client testClient1;
    private Client testClient2;

    @Before
    public void initializeBank() {
        testBank = newBank();

        testClient1 = newClient();
        SavingAccount testSavingAccount1 = newSavingAccount();
        CheckingAccount testCheckingAccount1 = newCheckingAccount();
        testCheckingAccount1.setBalance(-100.0f);
        testClient1.addAccount(testSavingAccount1);
        testClient1.addAccount(testCheckingAccount1);

        testClient2 = newSecondClient();
        SavingAccount testSavingAccount2 = newSecondSavingAccount();
        CheckingAccount testCheckingAccount2 = newSecondCheckingAccount();
        testClient2.addAccount(testSavingAccount2);
        testClient2.addAccount(testCheckingAccount2);

        testBank.addClient(testClient1);
        testBank.addClient(testClient2);
    }

    @Test
    public void testGetName() {
        assertEquals(BANK_NAME, BankInfoCalc.getName(testBank));
    }

    @Test
    public void testCalcNumberOfClients() {
        assertEquals(2, BankInfoCalc.calcNumberOfClients(testBank));
    }

    @Test
    public void testCalcNumberOfAccounts() {
        assertEquals(4, BankInfoCalc.calcNumberOfAccounts(testBank));
    }

    @Test
    public void testGetClientsSet() {
        Set<String> expected = testBank.getClients().keySet().stream().collect(Collectors.toSet());

        Set<String> actual = BankInfoCalc.getClientsSet(testBank);

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void testCalcDepositSum() {
        assertEquals(2 * SAVING_ACCOUNT_INITIAL_BALANCE + CHECKING_ACCOUNT_INITIAL_BALANCE,
                BankInfoCalc.calcDepositSum(testBank), 0.0f);
    }

    @Test
    public void testCalcCreditSum() {
        assertEquals(100.0f, BankInfoCalc.calcCreditSum(testBank), 0.0f);
    }

    @Test
    public void testClientsByCity() {
        Map<String, List<Client>> clients = BankInfoCalc.clientsByCity(testBank);

        assertTrue(clients.get(CLIENT_CITY).contains(testClient1));
        assertTrue(clients.get(CLIENT_SECOND_CLIENT_CITY).contains(testClient2));
    }
}