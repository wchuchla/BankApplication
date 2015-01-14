package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;
import com.luxoft.bankapp.validator.Validator;

import java.io.IOException;
import java.util.logging.Logger;

public class ATMContinueCommand implements Command {

	private final BankServerInfo bankServerInfo;

	private static final Logger LOGGER = Logger.getLogger(ATMContinueCommand.class.getName());

	public ATMContinueCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() throws ClassNotFoundException, IOException {
		String message = (String) bankServerInfo.getIn().readObject();
		while (Validator.yesNoValidator(message)) {
			System.out.println(message);
			ServerThread.sendObject(bankServerInfo.getOut(), "You entered wrong value. Please try again.");
			message = (String) bankServerInfo.getIn().readObject();
		}
		if ("No".equalsIgnoreCase(message)) {
			bankServerInfo.getAtmCommands().get("3").execute();
		}
	}

	@Override
	public String getCommandInfo() {
		return null;
	}
}