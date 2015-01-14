package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.daoexception.ClientNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.service.BankCommander;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBSelectClientCommand implements Command {

	private final ClientDAO clientDAO;

	private static final Logger LOGGER = Logger.getLogger(DBSelectClientCommand.class.getName());

	public DBSelectClientCommand(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}

	@Override
	public void execute() {
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter client name: ");
			String name = scanner.nextLine();
			BankCommander.activeClient = clientDAO.findClientByName(BankCommander.activeBank,
					name);
		} catch (ClientNotFoundException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		} catch (DAOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Select the active client");
	}
}