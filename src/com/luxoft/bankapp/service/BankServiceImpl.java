package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.*;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankServiceImpl implements BankService {

	private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + BankServiceImpl.class.getName());

	@Override
	public void addClient(Bank bank, Client client) throws ClientExistsException {
		if (bank.getClients().containsKey(client.getName())) {
			throw new ClientExistsException(client.getName());
		}
		bank.addClient(client);
	}

	@Override
	public void removeClient(Bank bank, String name) throws ClientNotExistsException {
		Client client = getClient(bank, name);
		bank.removeClient(client);
	}

	@Override
	public void addAccount(Client client, Account account) throws AccountExistsException {
		for (Account clientAccount : client.getAccounts()) {
			if (clientAccount.getAccountType().equals(account.getAccountType())) {
				throw new AccountExistsException(account.getAccountType());
			}
		}
		setActiveAccount(client, account);
		client.addAccount(account);
	}

	@Override
	public void setActiveAccount(Client client, Account account) {
		client.setActiveAccount(account);
	}

	@Override
	public Client getClient(Bank bank, String name) throws ClientNotExistsException {
		if (bank.getClients().containsKey(name)) {
			return bank.getClients().get(name);
		}
		throw new ClientNotExistsException(name);
	}

	@Override
	public Account getAccountByType(Client client, String type) throws AccountNotExistsException {
		for (Account account : client != null ? client.getAccounts() : null) {
			if (account.getAccountType().equalsIgnoreCase(type)) {
				return account;
			}
		}
		throw new AccountNotExistsException(type);
	}

	@Override
	public boolean isAccountNumberAvailable(Bank bank, int accountNumber) {
		for (Client client : bank.getClients().values()) {
			for (Account account : client.getAccounts()) {
				if (account.getAccountNumber() == accountNumber) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void transfer(Account payerAccount, Account beneficiaryAccount, float amount)
			throws NotEnoughFundsException {
		payerAccount.withdraw(amount);
		beneficiaryAccount.deposit(amount);
	}

	@Override
	public void saveClient(Client client, String filePath) {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(
				filePath))) {
			objectOutputStream.writeObject(client);
		} catch (IOException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public Client loadClient(String filePath) {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(
				filePath))) {
			return (Client) objectInputStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
}