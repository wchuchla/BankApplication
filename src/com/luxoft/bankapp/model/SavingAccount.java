package com.luxoft.bankapp.model;

import com.luxoft.bankapp.exception.NotEnoughFundsException;
import com.luxoft.bankapp.model.info.SavingAccountInfo;

import java.util.Map;

public class SavingAccount extends AbstractAccount {

    private static final long serialVersionUID = 1L;
    private static final String BALANCE = "balance";

    /**
     * Constructs a new saving account with the default initial balance (0.0)
     */
    public SavingAccount() {

    }

    /**
     * Constructs a new saving account with the specified initial balance
     *
     * @param balance the initial balance
     * @throws java.lang.IllegalArgumentException if the initial balance is negative
     */
    public SavingAccount(float balance) {
        super(balance);
    }

    public SavingAccount(SavingAccount savingAccount) {
        super(savingAccount);
    }

    /**
     * Constructs a new saving account from CheckingAccountInfo bean
     *
     * @param info the checking account's info
     * @throws java.lang.NullPointerException if the specified info is null
     */
    public SavingAccount(SavingAccountInfo info) {
        accountNumber = info.getAccountNumber();
        balance = info.getBalance();
    }

    @Override
    public String getAccountType() {
        return "Saving";
    }

    /**
     * @throws com.luxoft.bankapp.exception.NotEnoughFundsException if the amount is bigger than current balance
     */
    @Override
    public void withdraw(float amount) throws NotEnoughFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (balance < amount) {
            throw new NotEnoughFundsException(amount - balance);
        }
        balance -= amount;
    }

    @Override
    public void parseFeed(Map<String, String> feed) {
        balance = Float.parseFloat(feed.get(BALANCE));
    }

    @Override
    public void printReport() {
        System.out.print("Account type = Saving account, ");
        super.printReport();
    }

    @Override
    public String toString() {
        return "SavingAccount [id=" + id + ", accountNumber=" + accountNumber + ", balance=" + balance + "]";
    }

}