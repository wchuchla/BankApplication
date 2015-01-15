package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.ClientExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.enums.Gender;
import com.luxoft.bankapp.service.BankCommander;
import com.luxoft.bankapp.service.BankService;
import com.luxoft.bankapp.validator.Validator;

import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AddClientCommand implements Command {

    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + AddClientCommand.class.getName());
    private final BankService bankService;
    private final ClientDAO clientDAO;

    public AddClientCommand(BankService bankService, ClientDAO clientDAO) {
        this.bankService = bankService;
        this.clientDAO = clientDAO;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        String name = nameRequest(scanner);

        String gender = genderRequest(scanner);

        String email = emailRequest(scanner);

        String phoneNumber = phoneNumberRequest(scanner);

        String city = cityRequest(scanner);

        String initialOverdraft = initialOverdraftRequest(scanner);

        try {
            executeAddingClient(name, gender, email, phoneNumber, city, initialOverdraft);
        } catch (ClientExistsException e) {
            System.out.println(e.getMessage());
        } catch (DAOException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private String nameRequest(Scanner scanner) {
        System.out.println("Enter client's full name (e.g. John Mayer): ");
        String name = scanner.nextLine();
        while (!Validator.nameValidator(name)) {
            System.out.println("You entered invalid name. Please try again.");
            name = scanner.nextLine();
        }
        return name;
    }

    private String genderRequest(Scanner scanner) {
        System.out.println("Enter client's gender [MALE/FEMALE]: ");
        String gender = scanner.nextLine();
        while (!Validator.genderValidator(gender)) {
            System.out.println("You entered invalid gender. Please try again.");
            gender = scanner.nextLine();
        }
        return gender;
    }

    private String emailRequest(Scanner scanner) {
        System.out.println("Enter client's email (e.g. jmayer@gmail.com): ");
        String email = scanner.nextLine();
        while (!Validator.emailValidator(email)) {
            System.out.println("You entered invalid email. Please try again.");
            email = scanner.nextLine();
        }
        return email;
    }

    private String phoneNumberRequest(Scanner scanner) {
        System.out.println("Enter client's phone number (e.g. +001-123-456-789): ");
        String phoneNumber = scanner.nextLine();
        while (!Validator.phoneNumberValidator(phoneNumber)) {
            System.out.println("You entered invalid phone number. Please try again.");
            phoneNumber = scanner.nextLine();
        }
        return phoneNumber;
    }

    private String cityRequest(Scanner scanner) {
        System.out.println("Enter client's city (e.g. New York): ");
        String city = scanner.nextLine();
        while (!Validator.cityValidator(city)) {
            System.out.println("You entered invalid city. Please try again.");
            city = scanner.nextLine();
        }
        return city;
    }

    private String initialOverdraftRequest(Scanner scanner) {
        System.out.println("Enter client's initial overdraft (e.g. 10000): ");
        String initialOverdraft = scanner.nextLine();
        while (!Validator.isPositiveValidator(initialOverdraft)) {
            System.out.println("You entered invalid initial overdraft. Please try again.");
            initialOverdraft = scanner.nextLine();
        }
        return initialOverdraft;
    }

    private void executeAddingClient(String name, String gender, String email, String phoneNumber, String city, String initialOverdraft) throws DAOException, ClientExistsException {
        Client client = new Client(name, Gender.valueOf(gender), email, phoneNumber, city,
                Float.parseFloat(initialOverdraft));
        clientDAO.save(BankCommander.activeBank, client);
        bankService.addClient(BankCommander.activeBank, client);
        BankCommander.activeClient = client;
        System.out.println("Client " + client.getName() + " was successfully added");
    }

    @Override
    public void printCommandInfo() {
        System.out.print("Register new client");
    }
}