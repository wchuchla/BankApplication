package com.luxoft.bankapp.command;

import java.util.logging.Logger;

public class ExitCommand implements Command {

	private static final Logger LOGGER = Logger.getLogger(ExitCommand.class.getName());

	@Override
	public void execute() {
		System.exit(0);
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Exit");
	}
}