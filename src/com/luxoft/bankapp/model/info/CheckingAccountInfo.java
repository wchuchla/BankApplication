package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.CheckingAccount;

import java.io.Serializable;

public class CheckingAccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int accountNumber;
    private final float balance;
    private final float overdraft;

    public CheckingAccountInfo(CheckingAccount checkingAccount) {
        accountNumber = checkingAccount.getAccountNumber();
        balance = checkingAccount.getBalance();
        overdraft = checkingAccount.getOverdraft();
    }

    public CheckingAccountInfo(int accountNumber, float balance, float overdraft) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.overdraft = overdraft;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public float getOverdraft() {
        return overdraft;
    }

    @Override
    public String toString() {
        return "CheckingAccountInfo [accountNumber=" + accountNumber + ", balance=" + balance + ", overdraft="
                + overdraft + "]";
    }
}