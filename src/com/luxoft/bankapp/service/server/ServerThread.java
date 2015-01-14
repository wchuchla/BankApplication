package com.luxoft.bankapp.service.server;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.ClientService;
import com.luxoft.bankapp.service.server.command.*;
import com.luxoft.bankapp.validator.Validator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class ServerThread implements Runnable {

	private static final String REMOTEOFFICE = "remoteoffice";
	private static final String ATM = "atm";
	private static final Logger LOGGER = Logger.getLogger(ServerThread.class.getName());
	private final Bank currentBank;
	private final ClientService clientService;
	private String message;
	private Socket clientSocket = null;

	public ServerThread(Socket clientSocket, Bank currentBank, ClientService clientService) {
		this.clientSocket = clientSocket;
		this.currentBank = currentBank;
		this.clientService = clientService;
	}

	public static void sendObject(ObjectOutputStream out, final Object object) {
		try {
			out.writeObject(object);
			out.flush();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	private static void registerCommand(Map<String, Command> commands, String name, Command command) {
		commands.put(name, command);
	}

	@Override
	public void run() {
		try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			 ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

			out.flush();
			Client currentClient = null;

			Map<String, Command> atmCommandsMap = new TreeMap<>();
			Map<String, Command> remoteOfficeCommandsMap = new TreeMap<>();

			BankServerInfo bankServerInfo = new BankServerInfo(out, in, currentBank, clientService,
					atmCommandsMap, remoteOfficeCommandsMap);

			commandsInitialization(atmCommandsMap, remoteOfficeCommandsMap, bankServerInfo);

			message = (String) in.readObject();
			if (message.equals(ATM)) {
				do {
					do {
						if (currentClient == null) {
							currentClient = noActiveClientService(bankServerInfo);
						}
					} while (!bankServerInfo.isLogin());
					if (currentClient != null) {
						showActiveClientMenu(out, atmCommandsMap);
						activeClientService(out, in, atmCommandsMap);
					}
				} while (bankServerInfo.isLogin());
			} else if (message.equals(REMOTEOFFICE)) {
				do {
					bankServerInfo.setLogin(true);
					showRemoteOfficeMenu(out, remoteOfficeCommandsMap);
					remoteOfficeService(out, in, remoteOfficeCommandsMap);
				} while (bankServerInfo.isLogin());
			}
		} catch (ClassNotFoundException | NumberFormatException | IOException e) {
			LOGGER.error(e);
		} finally {
			BankServerThreaded.CLIENT_COUNTER.decrementAndGet();
		}
	}

	private Client noActiveClientService(BankServerInfo bankServerInfo) throws ClassNotFoundException, IOException {
		Client currentClient;
		currentClient = (new ATMLoginCommand(bankServerInfo)).execute();
		bankServerInfo.setCurrentClient(currentClient);
		return currentClient;
	}

	private void showRemoteOfficeMenu(ObjectOutputStream out, Map<String, Command> remoteOfficeCommandsMap) {
		StringBuilder response = new StringBuilder("Remote Office Menu");
		for (String commandNumber : remoteOfficeCommandsMap.keySet()) {
			response.append("\n").append(commandNumber).append(") ");
			response.append(remoteOfficeCommandsMap.get(commandNumber).getCommandInfo());
		}
		sendObject(out, response.toString());
	}

	private void showActiveClientMenu(ObjectOutputStream out, Map<String, Command> atmCommandsMap) {
		StringBuilder response = new StringBuilder("ATM Menu");
		for (String commandNumber : atmCommandsMap.keySet()) {
			response.append("\n").append(commandNumber).append(") ");
			response.append(atmCommandsMap.get(commandNumber).getCommandInfo());
		}
		sendObject(out, response.toString());
	}

	private void activeClientService(ObjectOutputStream out, ObjectInputStream in, Map<String, Command> atmCommandsMap) throws IOException, ClassNotFoundException {
		message = (String) in.readObject();
		while (!Validator.atmClientCommandValidator(message)) {
			sendObject(out, "You entered invalid command number. Please try again.");
			message = (String) in.readObject();
		}
		atmCommandsMap.get(message).execute();
	}

	private void remoteOfficeService(ObjectOutputStream out, ObjectInputStream in, Map<String, Command> remoteOfficeCommandsMap) throws IOException, ClassNotFoundException {
		message = (String) in.readObject();
		while (!Validator.remoteOfficeCommandValidator(message)) {
			sendObject(out, "You entered invalid command number. Please try again.");
			message = (String) in.readObject();
		}
		remoteOfficeCommandsMap.get(message).execute();
	}

	private void commandsInitialization(Map<String, Command> atmCommandsMap,
										Map<String, Command> remoteOfficeCommandsMap, BankServerInfo bankServerInfo) {
		registerCommand(atmCommandsMap, "0", new ATMGetBalanceCommand(bankServerInfo));
		registerCommand(atmCommandsMap, "1", new ATMWithdrawalCommand(bankServerInfo));
		registerCommand(atmCommandsMap, "2", new ATMDepositCommand(bankServerInfo));
		registerCommand(atmCommandsMap, "3", new ATMCancelCommand(bankServerInfo));
		registerCommand(remoteOfficeCommandsMap, "0", new GetBankStatisticsCommand(bankServerInfo));
		registerCommand(remoteOfficeCommandsMap, "1", new ClientInformationCommand(bankServerInfo));
		registerCommand(remoteOfficeCommandsMap, "2", new AddClientCommand(bankServerInfo));
		registerCommand(remoteOfficeCommandsMap, "3", new AddAccountCommand(bankServerInfo));
		registerCommand(remoteOfficeCommandsMap, "4", new RemoveClientCommand(bankServerInfo));
		registerCommand(remoteOfficeCommandsMap, "5", new ExitCommand(bankServerInfo));
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}