package com.luxoft.bankapp.helper.entity;


import com.luxoft.bankapp.model.SavingAccount;

import static org.junit.Assert.*;

public class SavingAccountEntityHelper {

    public static final int SAVING_ACCOUNT_ACCOUNT_NUMBER = 10000000;
    public static final int SECOND_SAVING_ACCOUNT_ACCOUNT_NUMBER = 10000001;
    public static final String SAVING_ACCOUNT_TYPE = "C";
    public static final float SAVING_ACCOUNT_INITIAL_BALANCE = 100;

    private SavingAccountEntityHelper() {
        throw new UnsupportedOperationException("This class is a helper");
    }

    public static SavingAccount newSavingAccount() {
        SavingAccount savingAccount = new SavingAccount(SAVING_ACCOUNT_INITIAL_BALANCE);
        savingAccount.setAccountNumber(SAVING_ACCOUNT_ACCOUNT_NUMBER);
        return savingAccount;
    }

    public static SavingAccount newSecondSavingAccount() {
        SavingAccount savingAccount = new SavingAccount(SAVING_ACCOUNT_INITIAL_BALANCE);
        savingAccount.setAccountNumber(SECOND_SAVING_ACCOUNT_ACCOUNT_NUMBER);
        return savingAccount;
    }

    public static void assertSavingAccount(SavingAccount savingAccount) {
        assertNotNull(savingAccount);
        assertTrue(savingAccount.getId() > 0);
        assertEquals(SAVING_ACCOUNT_ACCOUNT_NUMBER, savingAccount.getAccountNumber());
        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE, savingAccount.getBalance(), 0.0f);
    }

    public static void assertSecondSavingAccount(SavingAccount savingAccount) {
        assertNotNull(savingAccount);
        assertTrue(savingAccount.getId() > 0);
        assertEquals(SECOND_SAVING_ACCOUNT_ACCOUNT_NUMBER, savingAccount.getAccountNumber());
        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE, savingAccount.getBalance(), 0.0f);
    }
}