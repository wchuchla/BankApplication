package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.io.IOException;
import java.util.logging.Logger;

public class ExitCommand implements Command {

	private final BankServerInfo bankServerInfo;

	private static final Logger LOGGER = Logger.getLogger(ExitCommand.class.getName());

	public ExitCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() throws ClassNotFoundException, IOException {
		ServerThread.sendObject(bankServerInfo.getOut(),
				"Thank you for use our services.\nPress enter to close connection.");
		bankServerInfo.setLogin(false);
	}

	@Override
	public String getCommandInfo() {
		return "Cancel";
	}
}