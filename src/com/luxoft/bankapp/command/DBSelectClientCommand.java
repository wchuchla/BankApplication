package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.daoexception.ClientNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.service.BankCommander;

import java.util.Scanner;

public class DBSelectClientCommand implements Command {

	private final ClientDAO clientDAO;

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
			System.out.println(e.getMessage() + ". Try again.\n");
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Select the active client");
	}
}