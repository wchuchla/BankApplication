package com.luxoft.bankapp.service;

import com.luxoft.bankapp.command.*;
import com.luxoft.bankapp.dao.AccountDAOImpl;
import com.luxoft.bankapp.dao.BankDAOImpl;
import com.luxoft.bankapp.dao.ClientDAOImpl;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.validator.Validator;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;

public class BankCommander {
	private static final Scanner SCANNER;
	private static final Logger LOGGER = Logger.getLogger(BankCommander.class.getName());

	static {
		SCANNER = new Scanner(System.in);
		COMMANDS = new TreeMap<>();

		registerCommand("00", new DBSelectBankCommand(new BankDAOImpl()));
		registerCommand("01", new DBSelectClientCommand(new ClientDAOImpl()));
		registerCommand("02", new AddClientCommand(new BankServiceImpl(), new ClientDAOImpl()));
		registerCommand("03", new AddAccountCommand(new BankServiceImpl(), new AccountDAOImpl()));
		registerCommand("04", new GetAccountsCommand());
		registerCommand("05", new WithdrawCommand(new BankServiceImpl(), new AccountDAOImpl()));
		registerCommand("06", new DepositCommand(new BankServiceImpl(), new AccountDAOImpl()));
		registerCommand("07", new TransferCommand(new BankServiceImpl(), new ClientDAOImpl(), new AccountDAOImpl()));
		registerCommand("08", new DBRemoveClientCommand(new BankServiceImpl(), new ClientDAOImpl()));
		registerCommand("09", new DBReportCommand());
		registerCommand("10", new ExitCommand());
	}

	private static final Map<String, Command> COMMANDS;
	public static Bank activeBank;
	public static Client activeClient;

	private BankCommander() {

	}

	public static void main(String[] args) {
		while (true) {
			while (activeBank == null) {
				noActiveBankService();
			}
			while (activeClient == null) {
				showNoActiveClientMenu();
				noActiveClientService();
			}
			showActiveClientMenu();
			activeClientService();
		}
	}

	private static void noActiveBankService() {
		System.out.println("Bank Commander Menu: \nPlease select active bank\n");
		COMMANDS.get("00").execute();
	}

	private static void showNoActiveClientMenu() {
		System.out.print("00) ");
		COMMANDS.get("00").printCommandInfo();
		System.out.print("\n01) ");
		COMMANDS.get("01").printCommandInfo();
		System.out.print("\n02) ");
		COMMANDS.get("02").printCommandInfo();
		System.out.print("\n09) ");
		COMMANDS.get("09").printCommandInfo();
		System.out.print("\n10) ");
		COMMANDS.get("10").printCommandInfo();
		System.out.println();
		System.out.println("\nWrite a command number: ");
	}

	private static void noActiveClientService() {
		String command;
		command = SCANNER.nextLine();
		while (!Validator.noActiveClientCommandValidator(command)) {
			System.out.println("You entered invalid command number. Please try again.");
			command = SCANNER.nextLine();
		}
		COMMANDS.get(command).execute();
	}

	private static void showActiveClientMenu() {
		System.out.println("Bank Commander Menu\nCurrent bank: " + activeBank.getName()
				+ "\nCurrent client: " + activeClient.getName());

		for (String commandNumber : COMMANDS.keySet()) {
			System.out.print("\n" + commandNumber + ") ");
			COMMANDS.get(commandNumber).printCommandInfo();
		}
		System.out.println("\nWrite a command number: ");
	}

	private static void activeClientService() {
		String command;
		command = SCANNER.nextLine();
		while (!Validator.clientCommandValidator(command)) {
			System.out.println("You entered invalid command number. Please try again.");
			command = SCANNER.nextLine();
		}
		COMMANDS.get(command).execute();
	}

	private static void registerCommand(String name, Command command) {
		COMMANDS.put(name, command);
	}
}