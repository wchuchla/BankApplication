package com.luxoft.bankapp.command;

import com.luxoft.bankapp.dao.AccountDAO;
import com.luxoft.bankapp.dao.ClientDAO;
import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.NotEnoughFundsException;
import com.luxoft.bankapp.exception.daoexception.ClientNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.BankCommander;
import com.luxoft.bankapp.service.BankService;
import com.luxoft.bankapp.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransferCommand implements Command {

    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + TransferCommand.class.getName());
    private final BankService bankService;
    private final ClientDAO clientDAO;
    private final AccountDAO accountDAO;

    public TransferCommand(BankService bankService, ClientDAO clientDAO, AccountDAO accountDAO) {
        this.bankService = bankService;
        this.clientDAO = clientDAO;
        this.accountDAO = accountDAO;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        printAccountList();

        String activeAccount = transferAccountRequest(scanner);

        setActiveAccount(activeAccount);

        String amount = amountRequest(scanner);

        String beneficiaryName = beneficiaryNameRequest(scanner);

        try {
            executeTransfer(amount, beneficiaryName);
        } catch (NotEnoughFundsException | ClientNotFoundException | AccountExistsException e) {
            System.out.println(e.getMessage());
        } catch (DAOException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void printAccountList() {
        int i = 0;
        System.out.println("List of accounts: ");
        for (Account account : BankCommander.activeClient.getAccounts()) {
            System.out.print(i++ + ") ");
            account.printReport();
            System.out.println();
        }
    }

    private String transferAccountRequest(Scanner scanner) {
        System.out.println("Select the account from which you want to transfer: ");
        String activeAccount = scanner.nextLine();
        while (Validator.activeAccountValidator(activeAccount)) {
            System.out.println("You entered wrong account number. Please try again.");
            activeAccount = scanner.nextLine();
        }
        return activeAccount;
    }

    private void setActiveAccount(String activeAccount) {
        List<Account> accountsList = new ArrayList<>();
        accountsList.addAll(BankCommander.activeClient.getAccounts());

        bankService.setActiveAccount(BankCommander.activeClient,
                accountsList.get(Integer.parseInt(activeAccount)));
    }

    private String amountRequest(Scanner scanner) {
        System.out.println("Enter the amount to transfer: ");
        String amount = scanner.nextLine();
        while (!Validator.isPositiveValidator(amount)) {
            System.out.println("You entered wrong amount value. Please try again.");
            amount = scanner.nextLine();
        }
        return amount;
    }

    private String beneficiaryNameRequest(Scanner scanner) {
        System.out.println("Enter the full name of beneficiary: ");
        String beneficiaryName = scanner.nextLine();
        while (!Validator.nameValidator(beneficiaryName)) {
            System.out.println("You entered wrong beneficiary name. Please try again.");
            beneficiaryName = scanner.nextLine();
        }
        return beneficiaryName;
    }

    private void executeTransfer(String amount, String beneficiaryName) throws NotEnoughFundsException, DAOException, AccountExistsException {
        Client beneficiary = clientDAO.findClientByName(BankCommander.activeBank, beneficiaryName);
        bankService.transfer(BankCommander.activeClient.getActiveAccount(),
                beneficiary.getActiveAccount(), Float.parseFloat(amount));
        accountDAO.save(BankCommander.activeClient,
                BankCommander.activeClient.getActiveAccount());
        accountDAO.save(beneficiary, beneficiary.getActiveAccount());
        System.out.println("Transfer executed. Balance after transfer: "
                + BankCommander.activeClient.getActiveAccount().getBalance() + ".");
    }

    @Override
    public void printCommandInfo() {
        System.out.print("Make a transfer");
    }
}