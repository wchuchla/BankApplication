package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.ClientNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.service.BankCommander;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBSelectClientCommand implements Command {

	private final ClientDAO clientDAO;

	private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + DBSelectClientCommand.class.getName());

	public DBSelectClientCommand(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}

	@Override
	public void execute() {
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter client's name: ");
			String name = scanner.nextLine();
			BankCommander.activeClient = clientDAO.findClientByName(BankCommander.activeBank,
					name);
		} catch (ClientNotFoundException | AccountExistsException e) {
			System.out.println(e.getMessage());
		} catch (DAOException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Select the active client");
	}
}