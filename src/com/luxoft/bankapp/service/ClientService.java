package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.*;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;

public interface ClientService {
	Client getClient(String name) throws ClientNotExistsException;

	Account getAccount(Client client, String type) throws AccountNotExistsException;

	float getBalance(Account account);

	void deposit(Account account, float amount);

	void withdraw(Account account, float amount) throws NotEnoughFundsException;

	public void addClient(Bank bank, Client client) throws ClientExistsException;

	void removeClient(Bank bank, String name) throws ClientNotExistsException;

	public void addAccount(Client client, Account account) throws AccountExistsException;

	boolean isAccountNumberAvailable(Bank bank, int accountNumber);
}