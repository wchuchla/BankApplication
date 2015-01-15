package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.ClientNotExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.service.BankCommander;
import com.luxoft.bankapp.service.BankService;
import com.luxoft.bankapp.validator.Validator;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBRemoveClientCommand implements Command {

    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + DBRemoveClientCommand.class.getName());
    private final BankService bankService;
    private final ClientDAO clientDAO;

    public DBRemoveClientCommand(BankService bankService, ClientDAO clientDAO) {
        this.bankService = bankService;
        this.clientDAO = clientDAO;
    }

    @Override
    public void execute() {
        try {
            Scanner scanner = new Scanner(System.in);
            String answer = removeConfirmation(scanner);
            if ("Yes".equalsIgnoreCase(answer)) {
                executeRemovingClient();
            }
        } catch(ClientNotExistsException e) {
            System.out.println(e.getMessage());
        } catch (DAOException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private String removeConfirmation(Scanner scanner) {
        System.out.println("Are you sure to delete client "
                + BankCommander.activeClient.getName() + "? (Yes/yes/No/no)");
        String answer = scanner.nextLine();
        while (Validator.yesNoValidator(answer)) {
            System.out.println(answer);
            System.out.println("You entered wrong value. Please try again.");
            answer = scanner.nextLine();
        }
        return answer;
    }

    private void executeRemovingClient() throws DAOException, ClientNotExistsException {
        clientDAO.remove(BankCommander.activeBank, BankCommander.activeClient);
        bankService.removeClient(BankCommander.activeBank,
                BankCommander.activeClient.getName());
        BankCommander.activeClient = null;
        System.out.println("Client was successfully deleted");
    }

    @Override
    public void printCommandInfo() {
        System.out.print("Remove the active client");
    }
}