package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.ClientNotExistsException;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.model.info.CheckingAccountInfo;
import com.luxoft.bankapp.model.info.SavingAccountInfo;
import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.io.IOException;

public class AddAccountCommand implements Command {

	private final BankServerInfo bankServerInfo;

	public AddAccountCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() throws ClassNotFoundException, IOException {
		try {
			ServerThread.sendObject(bankServerInfo.getOut(), "Enter client name: ");
			String message = (String) bankServerInfo.getIn().readObject();
			Client client = bankServerInfo.getClientService().getClient(message);

			ServerThread.sendObject(bankServerInfo.getOut(), "Send AccountInfo object:");
			Object accountInfo = bankServerInfo.getIn().readObject();

			if (accountInfo instanceof SavingAccountInfo) {
				SavingAccountInfo savingAccountInfo = (SavingAccountInfo) accountInfo;
				SavingAccount savingAccount = new SavingAccount(savingAccountInfo);
				if (bankServerInfo.getClientService().isAccountNumberAvailable(
						bankServerInfo.getCurrentBank(), savingAccount.getAccountNumber())) {
					bankServerInfo.getClientService().addAccount(client, savingAccount);
				} else {
					System.out.println("Account number is already used");
				}

			} else if (accountInfo instanceof CheckingAccountInfo) {
				CheckingAccountInfo checkingAccountInfo = (CheckingAccountInfo) accountInfo;
				CheckingAccount checkingAccount = new CheckingAccount(checkingAccountInfo);
				bankServerInfo.getClientService().addAccount(client, checkingAccount);
				if (bankServerInfo.getClientService().isAccountNumberAvailable(
						bankServerInfo.getCurrentBank(), checkingAccount.getAccountNumber())) {
					bankServerInfo.getClientService().addAccount(client, checkingAccount);
				} else {
					System.out.println("Account number is already used");
				}
			}

			ServerThread
					.sendObject(bankServerInfo.getOut(), "Account succesfully added\nDo you want to perform other operations? (Yes/yes/No/no)");
			new ContinueCommand(bankServerInfo).execute();
		} catch (ClientNotExistsException | AccountExistsException e) {
			ServerThread.sendObject(bankServerInfo.getOut(), e.getMessage() + "\nPress Enter to back to menu");
			bankServerInfo.getIn().readObject();
		}
	}

	@Override
	public String getCommandInfo() {
		return "Add account";
	}
}