package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.AccountDAO;
import com.luxoft.bankapp.exception.NotEnoughFundsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.service.BankCommander;
import com.luxoft.bankapp.service.BankService;
import com.luxoft.bankapp.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WithdrawCommand implements Command {

	private final BankService bankService;
	private final AccountDAO accountDAO;

	private static final Logger LOGGER = Logger.getLogger(WithdrawCommand.class.getName());

	public WithdrawCommand(BankService bankService, AccountDAO accountDAO) {
		this.bankService = bankService;
		this.accountDAO = accountDAO;
	}

	@Override
	public void execute() {
		Scanner scanner = new Scanner(System.in);

		printAccountList();

		String activeAccount = withdrawAccountRequest(scanner);

		String amount = amountRequest(scanner);

		setActiveAccount(activeAccount);

		try {
			executeWithdraw(amount);
		} catch (DAOException | NotEnoughFundsException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
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

	private String withdrawAccountRequest(Scanner scanner) {
		System.out.println("Select the account from which you want to withdraw: ");
		String activeAccount = scanner.nextLine();
		while (Validator.activeAccountValidator(activeAccount)) {
			System.out.println("You entered wrong account number. Please try again.");
			activeAccount = scanner.nextLine();
		}
		return activeAccount;
	}

	private String amountRequest(Scanner scanner) {
		System.out.println("Enter the amount to withdraw: ");
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

	private void executeWithdraw(String amount) throws DAOException, NotEnoughFundsException {
		BankCommander.activeClient.getActiveAccount().withdraw(Float.parseFloat(amount));
		accountDAO.save(BankCommander.activeClient,
				BankCommander.activeClient.getActiveAccount());
		System.out.println("Withdraw operation executed. Balance after deposit: "
				+ BankCommander.activeClient.getActiveAccount().getBalance() + ".");
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Make a withdraw");
	}
}