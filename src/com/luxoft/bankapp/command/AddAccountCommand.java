package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.AccountDAO;
import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.BankCommander;
import com.luxoft.bankapp.service.BankService;
import com.luxoft.bankapp.validator.Validator;

import java.util.Scanner;

public class AddAccountCommand implements Command {

	private final BankService bankService;
	private final AccountDAO accountDAO;

	public AddAccountCommand(BankService bankService, AccountDAO accountDAO) {
		this.bankService = bankService;
		this.accountDAO = accountDAO;
	}

	@Override
	public void execute() {
		String accountType = accountTypeRequest();
		try {
			if ("saving".equalsIgnoreCase(accountType)) {
				addSavingAccount();
			} else if ("checking".equalsIgnoreCase(accountType)) {
				addCheckingAccount();
			}
		} catch (AccountExistsException e) {
			System.out.println(e.getMessage());
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	private String accountTypeRequest() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Select account's type: (Saving, Checking): ");
		String accountType = scanner.nextLine();
		while (!Validator.accountTypeValidator(accountType)) {
			System.out.println("You entered invalid account type. Please try again.");
			accountType = scanner.nextLine();
		}
		return accountType;
	}

	private void addSavingAccount() throws AccountExistsException, DAOException {
		SavingAccount account = new SavingAccount(0);
		bankService.addAccount(BankCommander.activeClient, account);
		bankService.setActiveAccount(BankCommander.activeClient, account);
		accountDAO.save(BankCommander.activeClient, account);
		System.out.println("Saving account was created.");
	}

	private void addCheckingAccount() throws AccountExistsException, DAOException {
		CheckingAccount account = new CheckingAccount(0, BankCommander.activeClient.getInitialOverdraft());
		bankService.addAccount(BankCommander.activeClient, account);
		bankService.setActiveAccount(BankCommander.activeClient, account);
		accountDAO.save(BankCommander.activeClient, account);
		System.out.println("Checking account was created.");
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Create new account");
	}
}