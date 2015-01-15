package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.SavingAccount;

import java.io.Serializable;

public class SavingAccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int accountNumber;
    private final float balance;

    public SavingAccountInfo(SavingAccount savingAccount) {
        accountNumber = savingAccount.getAccountNumber();
        balance = savingAccount.getBalance();
    }

    public SavingAccountInfo(int accountNumber, float balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "SavingAccountInfo [accountNumber=" + accountNumber + ", balance=" + balance +
                "]";
    }
}