package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.BankDAO;
import com.luxoft.bankapp.exception.daoexception.BankNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.service.BankCommander;

import java.util.Scanner;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBSelectBankCommand implements Command {

	private final BankDAO bankDAO;

	private static final Logger LOGGER = Logger.getLogger(DBSelectBankCommand.class.getName());
	public DBSelectBankCommand(BankDAO bankDAO) {
		this.bankDAO = bankDAO;
	}


	@Override
	public void execute() {
		try {
			Scanner scanner = new Scanner(System.in);
			String name = activeBankRequest(scanner);

			getBank(name);

		} catch (BankNotFoundException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		} catch (DAOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private String activeBankRequest(Scanner scanner) {
		System.out.println("Enter bank name: ");
		return scanner.nextLine();
	}

	private void getBank(String name) throws DAOException {
		BankCommander.activeBank = bankDAO.getBankByName(name);
	}

	@Override
	public void printCommandInfo() {
		System.out.print("Select the active bank");
	}

}