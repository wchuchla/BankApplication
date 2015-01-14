package com.luxoft.bankapp.service.remoteoffice;

import com.luxoft.bankapp.model.enums.Gender;
import com.luxoft.bankapp.model.info.BankInfo;
import com.luxoft.bankapp.model.info.CheckingAccountInfo;
import com.luxoft.bankapp.model.info.ClientInfo;
import com.luxoft.bankapp.model.info.SavingAccountInfo;
import com.luxoft.bankapp.validator.Validator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class BankRemoteOffice implements Runnable {
	private static final String CLIENT_TYPE = "remoteoffice";
	private static final int PORT = 2004;
	private static final String SERVER = "localhost";
	private static final String CLOSE_CONNECTION = "Thank you for use our services.\nPress enter to close connection.";
	private static final Scanner SCANNER = new Scanner(System.in);
	private static final Logger LOGGER = Logger.getLogger(BankRemoteOffice.class.getName());
	private Object object;
	private String message;

	public static void main(String args[]) {
		BankRemoteOffice client = new BankRemoteOffice();
		client.run();
	}

	@Override
	public void run() {
		try (Socket requestSocket = new Socket(SERVER, PORT);
			 ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
			 ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream())) {

			out.flush();

			System.out.println("Connected to localhost in port 2004");

			sendObject(out, CLIENT_TYPE);
			do {
				try {
					object = in.readObject();
					if (object instanceof String) {
						stringMessageCase(out);
					} else if (object instanceof BankInfo) {
						bankInfoMessageCase();
					} else if (object instanceof ClientInfo) {
						clientInfoMessageCase();
					} else {
						throw new ClassNotFoundException();
					}
				} catch (ClassNotFoundException e) {
					System.err.println("Data received in unknown format");
				}
			} while (!message.equals(CLOSE_CONNECTION));
		} catch (UnknownHostException e) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	private void clientInfoMessageCase() {
		ClientInfo clientInfo = (ClientInfo) object;
		System.out.println(clientInfo);
	}

	private void bankInfoMessageCase() {
		BankInfo bankInfo = (BankInfo) object;
		System.out.println(bankInfo);
	}

	private void stringMessageCase(ObjectOutputStream out) {
		message = (String) object;
		switch (message) {
			case "Send ClientInfo object:":
				addClient(out);
				break;
			case "Send AccountInfo object:":
				addAccount(out);
				break;
			default:
				System.out.println("server>" + message);
				sendObject(out, SCANNER.nextLine());
				break;
		}
	}

	private void addClient(ObjectOutputStream out) {
		String name = nameRequest();

		String gender = genderRequest();

		String email = emailRequest();

		String phoneNumber = phoneNumberRequest();

		String city = cityRequest();

		String initialOverdraft = initialOverdraftRequest();

		sendClientInfo(out, name, gender, email, phoneNumber, city, initialOverdraft);
	}

	private String nameRequest() {
		System.out.println("Enter client name (e.g. John Mayer): ");
		String name = SCANNER.nextLine();
		while (!Validator.nameValidator(name)) {
			System.out.println("You entered invalid name. Please try again.");
			name = SCANNER.nextLine();
		}
		return name;
	}

	private String genderRequest() {
		System.out.println("Enter client gender [MALE/FEMALE]: ");
		String gender = SCANNER.nextLine();
		while (!Validator.genderValidator(gender)) {
			System.out.println("You entered invalid gender. Please try again.");
			gender = SCANNER.nextLine();
		}
		return gender;
	}

	private String emailRequest() {
		System.out.println("Enter client email (e.g. jmayer@gmail.com): ");
		String email = SCANNER.nextLine();
		while (!Validator.emailValidator(email)) {
			System.out.println("You entered invalid email. Please try again.");
			email = SCANNER.nextLine();
		}
		return email;
	}

	private String phoneNumberRequest() {
		System.out.println("Enter client phone number (e.g. +48-123-456-789): ");
		String phoneNumber = SCANNER.nextLine();
		while (!Validator.phoneNumberValidator(phoneNumber)) {
			System.out.println("You entered invalid phone number. Please try again.");
			phoneNumber = SCANNER.nextLine();
		}
		return phoneNumber;
	}

	private String cityRequest() {
		System.out.println("Enter client city (e.g. New York): ");
		String city = SCANNER.nextLine();
		while (!Validator.cityValidator(city)) {
			System.out.println("You entered invalid city. Please try again.");
			city = SCANNER.nextLine();
		}
		return city;
	}

	private String initialOverdraftRequest() {
		System.out.println("Enter client initial overdraft (e.g. 10000): ");
		String initialOverdraft = SCANNER.nextLine();
		while (!Validator.isPositiveValidator(initialOverdraft)) {
			System.out.println("You entered invalid initial overdraft. Please try again.");
			initialOverdraft = SCANNER.nextLine();
		}
		return initialOverdraft;
	}

	private void sendClientInfo(ObjectOutputStream out, String name, String gender, String email, String phoneNumber, String city, String initialOverdraft) {
		ClientInfo clientInfo = new ClientInfo(name, Gender.valueOf(gender), email, phoneNumber,
				city, Float.parseFloat(initialOverdraft));

		sendObject(out, clientInfo);
	}

	private void addAccount(ObjectOutputStream out) {
		String accountType = accountTypeRequest();

		String accountNumber = accountNumberRequest();

		String balance = accountBalanceRequest();

		if ("checking".equalsIgnoreCase(accountType)) {
			String overdraft = overdraftRequest();
			sendCheckingAccountInfo(out, accountNumber, balance, overdraft);
		} else if ("saving".equalsIgnoreCase(accountType)) {
			sendSavingAccountInfo(out, accountNumber, balance);
		}
	}


	private String accountTypeRequest() {
		System.out.println("Select an account type\nC(c)hecking\nS(s)aving");
		String accountType = SCANNER.nextLine();
		while (!Validator.accountTypeValidator(accountType)) {
			System.out.println("You entered invalid account type. Please try again.");
			accountType = SCANNER.nextLine();
		}
		return accountType;
	}

	private String accountNumberRequest() {
		System.out.println("Enter account number (e.g. 10200050): ");
		String accountNumber = SCANNER.nextLine();
		while (!Validator.accountNumberValidator(accountNumber)) {
			System.out.println("You entered invalid account number. Please try again.");
			accountNumber = SCANNER.nextLine();
		}
		return accountNumber;
	}

	private String accountBalanceRequest() {
		System.out.println("Enter account balance (e.g. 10000): ");
		String balance = SCANNER.nextLine();
		while (!Validator.isDecimalValidator(balance)) {
			System.out.println("You entered invalid balance. Please try again.");
			balance = SCANNER.nextLine();
		}
		return balance;
	}

	private String overdraftRequest() {
		System.out.println("Enter account overdraft (e.g. 10000): ");
		String overdraft = SCANNER.nextLine();
		while (!Validator.isPositiveValidator(overdraft)) {
			System.out.println("You entered invalid balance. Please try again.");
			overdraft = SCANNER.nextLine();
		}
		return overdraft;
	}

	private void sendCheckingAccountInfo(ObjectOutputStream out, String accountNumber, String balance, String overdraft) {
		CheckingAccountInfo checkingAccountInfo = new CheckingAccountInfo(
				Integer.parseInt(accountNumber), Float.parseFloat(balance),
				Float.parseFloat(overdraft));
		sendObject(out, checkingAccountInfo);
	}

	private void sendSavingAccountInfo(ObjectOutputStream out, String accountNumber, String balance) {
		SavingAccountInfo savingAccountInfo = new SavingAccountInfo(
				Integer.parseInt(accountNumber), Float.parseFloat(balance));
		sendObject(out, savingAccountInfo);
	}

	void sendObject(ObjectOutputStream out, final Object object) {
		try {
			out.writeObject(object);
			out.flush();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
}