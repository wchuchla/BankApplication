package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.exception.ClientNotExistsException;
import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;
import com.luxoft.bankapp.validator.Validator;

import java.io.IOException;
import java.util.logging.Logger;

public class RemoveClientCommand implements Command {

	private final BankServerInfo bankServerInfo;

	private static final Logger LOGGER = Logger.getLogger(RemoveClientCommand.class.getName());

	public RemoveClientCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() throws ClassNotFoundException, IOException {
		try {
			ServerThread.sendObject(bankServerInfo.getOut(), "Enter client name: ");
			String message = (String) bankServerInfo.getIn().readObject();
			String name = message;

			ServerThread.sendObject(bankServerInfo.getOut(), "Are you sure? (Yes/yes/No/no)");
			message = (String) bankServerInfo.getIn().readObject();
			while (Validator.yesNoValidator(message)) {
				System.out.println(message);
				ServerThread.sendObject(bankServerInfo.getOut(), "You entered wrong value");
				message = (String) bankServerInfo.getIn().readObject();
			}
			if ("Yes".equalsIgnoreCase(message)) {
				bankServerInfo.getClientService().removeClient(bankServerInfo.getCurrentBank(), name);
				ServerThread
						.sendObject(bankServerInfo.getOut(),
								"Client successfully removed\nDo you want to perform other operations? " +
										"(Yes/yes/No/no)");
				new ContinueCommand(bankServerInfo).execute();
			} else {
				ServerThread.sendObject(bankServerInfo.getOut(),
						"Client wasn't removed\n\nPress Enter to back to menu");
				bankServerInfo.getIn().readObject();
			}

		} catch (ClientNotExistsException e) {
			ServerThread.sendObject(bankServerInfo.getOut(), e.getMessage()
					+ "\nPress Enter to back to menu");
			bankServerInfo.getIn().readObject();
		}
	}

	@Override
	public String getCommandInfo() {
		return "Remove client";
	}
}