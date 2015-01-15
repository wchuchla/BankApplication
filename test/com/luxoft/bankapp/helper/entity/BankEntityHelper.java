package com.luxoft.bankapp.helper.entity;

import com.luxoft.bankapp.model.Bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BankEntityHelper {

    public static final int BANK_ID = 1;
    public static final String BANK_NAME = "testBank";
    public static final String BANK_INVALID_NAME = "invalidName";

    private BankEntityHelper() {
        throw new UnsupportedOperationException("This class is a helper");
    }

    public static Bank newBank() {
        return new Bank(BANK_NAME);
    }

    public static void assertBank(Bank bank) {
        assertNotNull(bank);
        assertEquals(BANK_ID, bank.getId());
        assertEquals(BANK_NAME, bank.getName());
    }
}