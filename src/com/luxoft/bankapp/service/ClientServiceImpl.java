package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.*;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;

public class ClientServiceImpl implements ClientService {

    private final Bank activeBank;
    private final BankService bankService;

    public ClientServiceImpl(Bank activeBank, BankService bankService) {
        this.activeBank = activeBank;
        this.bankService = bankService;
    }

    @Override
    public synchronized Account getAccount(Client client, String type) throws AccountNotExistsException {
        return bankService.getAccountByType(client, type);
    }

    @Override
    public synchronized Client getClient(String name) throws ClientNotExistsException {
        return bankService.getClient(activeBank, name);
    }

    @Override
    public synchronized float getBalance(Account account) {
        return account.getBalance();
    }

    @Override
    public synchronized void deposit(Account account, float amount) {
        account.deposit(amount);
    }

    @Override
    public synchronized void withdraw(Account account, float amount) throws NotEnoughFundsException {
        account.withdraw(amount);
    }

    @Override
    public synchronized void addClient(Bank bank, Client client) throws ClientExistsException {
        bankService.addClient(bank, client);
    }

    @Override
    public synchronized void removeClient(Bank bank, String name) throws ClientNotExistsException {
        bankService.removeClient(bank, name);
    }

    @Override
    public synchronized void addAccount(Client client, Account account) throws AccountExistsException {
        bankService.addAccount(client, account);
    }

    @Override
    public synchronized boolean isAccountNumberAvailable(Bank bank, int accountNumber) {
        return bankService.isAccountNumberAvailable(bank, accountNumber);
    }
}