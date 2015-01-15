package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.exception.ClientNotExistsException;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.info.ClientInfo;
import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.io.IOException;
import java.util.logging.Logger;

public class ClientInformationCommand implements Command {

	private final BankServerInfo bankServerInfo;

	public ClientInformationCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() throws ClassNotFoundException, IOException {
		try {
			ServerThread.sendObject(bankServerInfo.getOut(), "Enter client name:");
			String message = (String) bankServerInfo.getIn().readObject();
			Client client = bankServerInfo.getClientService().getClient(message);
			ClientInfo clientInfo = new ClientInfo(client);
			ServerThread.sendObject(bankServerInfo.getOut(), clientInfo);
			ServerThread
					.sendObject(
							bankServerInfo.getOut(),
							"Client information was successfully sent.\nDo you want to perform other operations? " +
									"(Yes/yes/No/no)");
			new ContinueCommand(bankServerInfo).execute();
		} catch (ClientNotExistsException e) {
			ServerThread.sendObject(bankServerInfo.getOut(), e.getMessage()
					+ "\nPress Enter to back to menu");
			bankServerInfo.getIn().readObject();
		}
	}

	@Override
	public String getCommandInfo() {
		return "Get client information";
	}
}