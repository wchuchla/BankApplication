package com.luxoft.bankapp.command;

public class ExitCommand implements Command {

	@Override
	public void execute() {
		System.exit(0);
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Exit");
	}
}