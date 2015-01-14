package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;

import java.util.*;
import java.util.stream.Collectors;

public class BankInfoCalc {

	private BankInfoCalc() {

	}

	public static String getName(Bank bank) {
		return bank.getName();
	}

	public static int calcNumberOfClients(Bank bank) {
		return bank.getClients().size();
	}

	public static int calcNumberOfAccounts(Bank bank) {
		int numberOfAccounts = 0;
		for (Client client : bank.getClients().values()) {
			numberOfAccounts += client.getAccounts().size();
		}
		return numberOfAccounts;
	}

	public static Set<String> getClientsSet(Bank bank) {
		return bank.getClients().values().stream().map(Client::getName).collect(Collectors.toCollection(TreeSet::new));
	}

	public static float calcDepositSum(Bank bank) {
		float depositSum = 0;
		List<Account> accountsList = new LinkedList<>();
		for (Client client : bank.getClients().values()) {
			accountsList.addAll(client.getAccounts());
		}
		for (Account account : accountsList) {
			if (account.getBalance() > 0) {
				depositSum += account.getBalance();
			}
		}
		return depositSum;
	}

	public static float calcCreditSum(Bank bank) {
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
		return creditSum;
	}

	public static Map<String, List<Client>> clientsByCity(Bank bank) {
		Map<String, List<Client>> map = new TreeMap<>();
		for (Client client : bank.getClients().values()) {
			if (!map.containsKey(client.getCity())) {
				map.put(client.getCity(), new ArrayList<>());
			}
			map.get(client.getCity()).add(client);
		}
		return map;
	}
}