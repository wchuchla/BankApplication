package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.AccountDAO;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.service.BankCommander;
import com.luxoft.bankapp.service.BankService;
import com.luxoft.bankapp.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DepositCommand implements Command {

	private final BankService bankService;
	private final AccountDAO accountDAO;

	public DepositCommand(BankService bankService, AccountDAO accountDAO) {
		this.bankService = bankService;
		this.accountDAO = accountDAO;
	}

	@Override
	public void execute() {
		Scanner scanner = new Scanner(System.in);

		printAccountList();

		String activeAccount = depositAccountRequest(scanner);

		String amount = amountRequest(scanner);

		setActiveAccount(activeAccount);

		try {
			executeDeposit(amount);
		} catch (DAOException e) {
			e.getMessage();
		}
	}

	private void printAccountList() {
		int i = 0;
		System.out.println("List of accounts: ");
		for (Account account : BankCommander.activeClient.getAccounts()) {
			System.out.print(i++ + ") ");
			account.printReport();
			System.out.println();
		}
	}

	private String depositAccountRequest(Scanner scanner) {
		System.out.println("Select the account you want to deposit: ");
		String activeAccount = scanner.nextLine();
		while (Validator.activeAccountValidator(activeAccount)) {
			System.out.println("You entered wrong account number. Please try again.");
			activeAccount = scanner.nextLine();
		}
		return activeAccount;
	}

	private String amountRequest(Scanner scanner) {
		System.out.println("Enter the amount to deposit: ");
		String amount = scanner.nextLine();
		while (!Validator.isPositiveValidator(amount)) {
			System.out.println("You entered wrong amount value. Please try again.");
			amount = scanner.nextLine();
		}
		return amount;
	}

	private void setActiveAccount(String activeAccount) {
		List<Account> accountsList = new ArrayList<>();
		accountsList.addAll(BankCommander.activeClient.getAccounts());

		bankService.setActiveAccount(BankCommander.activeClient,
				accountsList.get(Integer.parseInt(activeAccount)));
	}

	private void executeDeposit(String amount) throws DAOException {
		BankCommander.activeClient.getActiveAccount().deposit(Float.parseFloat(amount));
		accountDAO.save(BankCommander.activeClient,
				BankCommander.activeClient.getActiveAccount());
		System.out.println("Deposit operation executed. Balance after deposit: "
				+ BankCommander.activeClient.getActiveAccount().getBalance() + ".");

	}

	@Override
	public void printCommandInfo() {
		System.out.print("Make a deposit");
	}
}