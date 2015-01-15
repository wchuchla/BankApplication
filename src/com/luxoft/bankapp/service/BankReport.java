package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;

import java.util.*;

public class BankReport {

    private BankReport() {

    }

    public static void getNumberOfClients(Bank bank) {
        System.out.println("The number of bank clients = " + bank.getClients().values().size());
    }

    public static void getAccountsNumber(Bank bank) {
        int counter = 0;
        for (Client client : bank.getClients().values()) {
            counter += client.getAccounts().size();
        }
        System.out.println("The total number of accounts for all bank clients = " + counter);
    }

    public static void getClientsSorted(Bank bank) {
        List<Account> accountsList = new LinkedList<>();
        for (Client client : bank.getClients().values()) {
            accountsList.addAll(client.getAccounts());
        }
        Collections.sort(accountsList, new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return Float.compare(o1.getBalance(), o2.getBalance());
            }
        });
        System.out.println("List of all accounts sorted by balance: ");
        for (Account account : accountsList) {
            account.printReport();
            System.out.println();
        }
    }

    public static void getBankCreditSum(Bank bank) {
        float creditSum = 0;
        List<Account> accountsList = new LinkedList<>();
        for (Client client : bank.getClients().values()) {
            accountsList.addAll(client.getAccounts());
        }
        for (Account account : accountsList) {
            if (account.getBalance() < 0) {
                creditSum -= account.getBalance();
            }
        }
        System.out.println("Total amount of credits granted to the bank clients = " + creditSum);
    }

    public static void getClientsByCity(Bank bank) {
        Map<String, List<Client>> map = new TreeMap<>();

        for (Client client : bank.getClients().values()) {
            if (!map.containsKey(client.getCity())) {
                map.put(client.getCity(), new ArrayList<>());
            }
            map.get(client.getCity()).add(client);
        }

        System.out.println("List of clients by cities: ");
        for (String city : map.keySet()) {
            List<Client> clients = map.get(city);
            System.out.print(city + ": ");
            for (Client client : clients) {
                System.out.print(client.getName() + ", ");
            }
            System.out.println();
        }
    }
}