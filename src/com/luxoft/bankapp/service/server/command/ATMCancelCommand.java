package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.util.logging.Logger;

public class ATMCancelCommand implements Command {

	private final BankServerInfo bankServerInfo;

	public ATMCancelCommand(BankServerInfo bankServerInfo) {
		this.bankServerInfo = bankServerInfo;
	}

	@Override
	public void execute() {
		ServerThread
				.sendObject(bankServerInfo.getOut(),
						"Thank you for use our services.\nPlease remove your card\nPress enter to close connection.");
		bankServerInfo.setLogin(false);
	}

	@Override
	public String getCommandInfo() {
		return "Cancel";
	}
}