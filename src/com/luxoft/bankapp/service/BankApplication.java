package com.luxoft.bankapp.service;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.BankException;
import com.luxoft.bankapp.exception.ClientExistsException;
import com.luxoft.bankapp.exception.ClientNotExistsException;
import com.luxoft.bankapp.model.*;
import com.luxoft.bankapp.model.enums.Gender;

import java.util.logging.Level;
import java.util.logging.Logger;


public class BankApplication {

	private static final String WITHDRAW;
	private static final String DEPOSIT;

	private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + BankApplication.class.getName());

	static {
		WITHDRAW = "withdraw";
		DEPOSIT = "deposit";
	}

	public static void main(String[] args) {

		BankApplication bankApplication = new BankApplication();

		System.out.println("*************** Bank Initialization ******************\n");

		Bank money24 = initializeBank();

		if (args.length == 1 && "-report".equals(args[0])) {
			BankReport.getNumberOfClients(money24);
			System.out.println();
			BankReport.getAccountsNumber(money24);
			System.out.println();
			BankReport.getBankCreditSum(money24);
			System.out.println();
			BankReport.getClientsSorted(money24);
			System.out.println();
			BankReport.getClientsByCity(money24);
		}

		System.out.println("*************** BankFeedService test ******************\n");

		bankApplication.printBankReport(money24);
	}

	private static Bank initializeBank() {
		Bank money24 = new Bank("Money24");
		BankService bankService = new BankServiceImpl();

		try {
			Client jSmith = new Client("John Smith", Gender.MALE, "jsmith@gmail.com",
					"+001-349-019-394", "New York", 5000);
			Account newCheckingAccount = new CheckingAccount(10000.99f, jSmith.getInitialOverdraft());
			bankService.addAccount(jSmith, newCheckingAccount);
			Account newSavingAccount = new SavingAccount(20000);
			bankService.addAccount(jSmith, newSavingAccount);
			bankService.setActiveAccount(jSmith, newSavingAccount);
			bankService.addClient(money24, jSmith);
		} catch (ClientExistsException | AccountExistsException e) {
			EXCEPTIONS_LOGGER.log(Level.WARNING, e.getMessage());
		}

		try {
			Client mJohnson = new Client("Mark Johnson", Gender.MALE, "mjohnson@gmail.com",
					"+001-349-019-394", "New York", 5000);
			Account newCheckingAccount = new CheckingAccount(10000, mJohnson.getInitialOverdraft());
			bankService.addAccount(mJohnson, newCheckingAccount);
			Account newSavingAccount = new SavingAccount(10000);
			bankService.addAccount(mJohnson, newSavingAccount);
			bankService.setActiveAccount(mJohnson, newSavingAccount);
			bankService.addClient(money24, mJohnson);
		} catch (IllegalArgumentException | ClientExistsException | AccountExistsException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		try {
			Client eWattson = new Client("Emma Wattson", Gender.FEMALE,
					"ewattson@gmail.com", "+001-123-456-798", "Washington", 10000);
			Account newSavingAccount = new SavingAccount(1000);
			bankService.addAccount(eWattson, newSavingAccount);
			bankService.setActiveAccount(eWattson, newSavingAccount);
			bankService.addClient(money24, eWattson);
		} catch (ClientExistsException | AccountExistsException e) {
			EXCEPTIONS_LOGGER.log(Level.WARNING, e.getMessage());
		}

		BankFeedService bankFeedService = new BankFeedService(money24);
		bankFeedService.loadFeed();

		return money24;
	}

	void printBankReport(Bank bank) {
		if (bank != null) {
			bank.printReport();
		} else {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, "Bank argument is null");
		}
	}

	public void modifyBank(Bank bank, Client client, Account account, String operation, float value) {

		if (bank != null && client != null && account != null && operation != null) {
			BankService bankService = new BankServiceImpl();
			try {
				bankService.setActiveAccount(client, account);
				if (operation.equalsIgnoreCase(DEPOSIT)) {
					client.getActiveAccount().deposit(value);
				} else if (operation.equalsIgnoreCase(WITHDRAW)) {
					client.getActiveAccount().withdraw(value);
				}
			} catch (BankException e) {
				EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		} else {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, "Some argument is null");
		}
	}

	public void removeClient(Bank bank, String name) {
		if (bank != null || name != null) {
			BankService bankService = new BankServiceImpl();
			try {
				bankService.removeClient(bank, name);
				EXCEPTIONS_LOGGER.log(Level.INFO, "Client " + name + " was successfully deleted.\n");
			} catch (ClientNotExistsException e) {
				EXCEPTIONS_LOGGER.log(Level.WARNING, e.getMessage());
			}
		} else {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, "Some argument is null");
		}
	}

	public void equalsTest(Client clientA, Client clientB) {
		System.out.println(clientA.getName() + " equals " + clientB.getName() + " = "
				+ clientA.equals(clientB));
	}
}