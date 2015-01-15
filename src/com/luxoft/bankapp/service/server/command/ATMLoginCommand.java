package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.exception.ClientNotExistsException;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.io.IOException;
import java.util.logging.Logger;

public class ATMLoginCommand {

	private final BankServerInfo bankServerInfo;

	public ATMLoginCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	public Client execute() throws ClassNotFoundException, IOException {
		try {
			ServerThread.sendObject(bankServerInfo.getOut(), "Enter your name: ");
			String message = (String) bankServerInfo.getIn().readObject();
			Client client = bankServerInfo.getClientService().getClient(message);
			bankServerInfo.setLogin(true);
			return client;
		} catch (ClientNotExistsException e) {
			ServerThread.sendObject(bankServerInfo.getOut(), e.getMessage()
					+ "\nPress Enter to try again");
			bankServerInfo.getIn().readObject();
		}
		return null;
	}
}