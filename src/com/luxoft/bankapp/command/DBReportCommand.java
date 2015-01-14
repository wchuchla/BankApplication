package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.BankDAO;
import com.luxoft.bankapp.dao.BankDAOImpl;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.info.BankInfo;
import com.luxoft.bankapp.validator.Validator;

import java.util.List;
import java.util.Scanner;

public class DBReportCommand implements Command {

	@Override
	public void execute() {
		Scanner scanner = new Scanner(System.in);
		BankDAO bankDAO = new BankDAOImpl();
		BankInfo bankInfo = bankDAO.getBankInfo();
		boolean exit = false;

		while (!exit) {
			showReportMenu();

			String command = scanner.nextLine();
			while (!Validator.dbReportCommandValidator(command)) {
				System.out.println("You entered invalid command number. Please try again.");
				command = scanner.nextLine();
			}

			switch (command) {
				case "0":
					printBankName(bankInfo);
					break;
				case "1":
					printNumberOfClients(bankInfo);
					break;
				case "2":
					printNumberOfAccounts(bankInfo);
					break;
				case "3":
					printListOfClients(bankInfo);
					break;
				case "4":
					printSumOfDeposits(bankInfo);
					break;
				case "5":
					printSumOfCredits(bankInfo);
					break;
				case "6":
					printListOfClientsByCity(bankInfo);
					break;
				default:
					break;
			}

			System.out.println("Do you want get other bank info? (Yes/yes/No/no)");
			String answer = scanner.nextLine();
			while (Validator.yesNoValidator(answer)) {
				System.out.println(answer);
				System.out.println("You entered wrong value. Please try again.");
				answer = scanner.nextLine();
			}
			if ("No".equalsIgnoreCase(answer)) {
				exit = true;
			}
		}
	}

	private void showReportMenu() {
		System.out.println("What info do you want: ");
		System.out.println("0) Bank name");
		System.out.println("1) Number of clients");
		System.out.println("2) Number of accounts");
		System.out.println("3) List of clients");
		System.out.println("4) Sum of deposits");
		System.out.println("5) Sum of credits");
		System.out.println("6) List of clients by city");
	}

	private void printBankName(BankInfo bankInfo) {
		System.out.println("Bank name: " + bankInfo.getName());
	}

	private void printNumberOfClients(BankInfo bankInfo) {
		System.out.println("Number of clients: " + bankInfo.getNumberOfClients());
	}

	private void printNumberOfAccounts(BankInfo bankInfo) {
		System.out.println("Number of accounts: " + bankInfo.getNumberOfAccounts());
	}

	private void printListOfClients(BankInfo bankInfo) {
		System.out.println("List of clients: ");
		bankInfo.getClientsSet().forEach(System.out::println);
	}

	private void printSumOfDeposits(BankInfo bankInfo) {
		System.out.println("Sum of deposits: " + bankInfo.getDepositSum());
	}

	private void printSumOfCredits(BankInfo bankInfo) {
		System.out.println("Sum of credits: " + bankInfo.getCreditSum());
	}

	private void printListOfClientsByCity(BankInfo bankInfo) {
		System.out.println("List of clients by city: ");
		for (String city : bankInfo.getClientsByCity().keySet()) {
			List<Client> clients = bankInfo.getClientsByCity().get(city);
			System.out.print(city + ": ");
			for (Client client : clients) {
				System.out.print(client.getName() + ", ");
			}
			System.out.println();
		}
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Print bank info");
	}

}