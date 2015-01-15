package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.SavingAccount;
import org.junit.Before;
import org.junit.Test;

import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;

public class SavingAccountInfoTest {
    private SavingAccountInfo sut;
    private SavingAccount testSavingAccount;

    @Before
    public void createSavingAccountWithBalance0AndOverdraft100() {
        testSavingAccount = newSavingAccount();
    }


    // test SavingAccountInfo(Account account)
    @Test
    public void testConstructorWithSavingAccountParameter() {
        sut = new SavingAccountInfo(testSavingAccount);

        assertEquals(SAVING_ACCOUNT_ACCOUNT_NUMBER, sut.getAccountNumber());
        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE, sut.getBalance(), 0.0f);
    }


    // test SavingAccountInfo(int accountNumber, float balance, float overdraft)
    @Test
    public void testConstructorWithAccountInfoParameters() {
        sut = new SavingAccountInfo(testSavingAccount.getAccountNumber(), testSavingAccount.getBalance());

        assertEquals(SAVING_ACCOUNT_ACCOUNT_NUMBER, sut.getAccountNumber());
        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE, sut.getBalance(), 0.0f);
    }


    // test toString()
    @Test
    public void testToString() {
        sut = new SavingAccountInfo(testSavingAccount);

        final String EXPECTED_STRING = "SavingAccountInfo [accountNumber=" + SAVING_ACCOUNT_ACCOUNT_NUMBER + ", " +
                "balance=" + SAVING_ACCOUNT_INITIAL_BALANCE + "]";

        assertEquals("toString() method does not produce the expected output",
                EXPECTED_STRING, sut.toString());
    }
}