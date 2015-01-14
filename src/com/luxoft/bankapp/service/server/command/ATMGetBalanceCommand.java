package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.exception.AccountNotExistsException;
import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;
import com.luxoft.bankapp.validator.Validator;

import java.io.IOException;
import java.util.logging.Logger;

public class ATMGetBalanceCommand implements Command {

	private final BankServerInfo bankServerInfo;

	private static final Logger LOGGER = Logger.getLogger(ATMGetBalanceCommand.class.getName());

	public ATMGetBalanceCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() throws ClassNotFoundException, IOException {
		try {
			ServerThread.sendObject(bankServerInfo.getOut(),
					"Select an account\nC(c)hecking\nS(s)aving");
			String message = (String) bankServerInfo.getIn().readObject();
			while (!Validator.accountTypeValidator(message)) {
				ServerThread.sendObject(bankServerInfo.getOut(),
						"You entered invalid account type. Please try again.");
				message = (String) bankServerInfo.getIn().readObject();
			}
			bankServerInfo.setCurrentAccount(bankServerInfo.getClientService().getAccount(
					bankServerInfo.getCurrentClient(), message));
			ServerThread.sendObject(bankServerInfo.getOut(), "Your account balance is: " + bankServerInfo.getClientService().getBalance(
					bankServerInfo.getCurrentAccount()) + "\nDo you want to perform other transaction? (Yes/yes/No/no)");
			new ATMContinueCommand(bankServerInfo).execute();
		} catch (AccountNotExistsException e) {
			ServerThread.sendObject(bankServerInfo.getOut(), e.getMessage()
					+ "\nPress Enter to back to menu");
			bankServerInfo.getIn().readObject();
		}
	}

	@Override
	public String getCommandInfo() {
		return "Balance Inquiry";
	}
}