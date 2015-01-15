package com.luxoft.bankapp.command;

import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.service.BankCommander;

import java.util.logging.Logger;

public class GetAccountsCommand implements Command {

	@Override
	public void execute() {
		int i = 0;
		System.out.println("List of accounts: ");
		for (Account account : BankCommander.activeClient.getAccounts()) {
			System.out.print(i++ + ") ");
			account.printReport();
			System.out.println();
		}
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Get list of account");
	}
}