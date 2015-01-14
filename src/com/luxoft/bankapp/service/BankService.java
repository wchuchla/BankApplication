package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.*;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;

public interface BankService {
	void addClient(Bank bank, Client client) throws ClientExistsException;

	void removeClient(Bank bank, String name) throws ClientNotExistsException;

	void addAccount(Client client, Account account) throws AccountExistsException;

	void setActiveAccount(Client client, Account account);

	public Account getAccountByType(Client client, String type) throws AccountNotExistsException;

	public boolean isAccountNumberAvailable(Bank bank, int accountNumber);

	void transfer(Account payerAccount, Account beneficiaryAccount, float amount)
			throws NotEnoughFundsException;

	Client getClient(Bank bank, String name) throws ClientNotExistsException;

	void saveClient(Client client, String filePath);

	Client loadClient(String filePath);
}