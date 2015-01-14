package com.luxoft.bankapp.service.server.command;

import java.io.IOException;

public interface Command {
	void execute() throws ClassNotFoundException, IOException;

	String getCommandInfo();
}