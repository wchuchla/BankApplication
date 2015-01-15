package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.exception.ClientExistsException;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.info.ClientInfo;
import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.io.IOException;
import java.util.logging.Logger;

public class AddClientCommand implements Command {

	private final BankServerInfo bankServerInfo;

	public AddClientCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() throws ClassNotFoundException, IOException {
		try {
			ServerThread.sendObject(bankServerInfo.getOut(), "Send ClientInfo object:");
			ClientInfo clientInfo = (ClientInfo) bankServerInfo.getIn().readObject();
			Client newClient = new Client(clientInfo);
			bankServerInfo.getClientService().addClient(bankServerInfo.getCurrentBank(), newClient);
			ServerThread
					.sendObject(bankServerInfo.getOut(), "Client successfully added\nDo you want to perform other " +
							"operations? (Yes/yes/No/no)");
			new ContinueCommand(bankServerInfo).execute();
		} catch (ClientExistsException e) {
			ServerThread.sendObject(bankServerInfo.getOut(), e.getMessage() + "\nPress Enter to back to menu");
			bankServerInfo.getIn().readObject();
		}
	}

	@Override
	public String getCommandInfo() {
		return "Add client";
	}
}