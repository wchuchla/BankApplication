package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.Client;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BankInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private int numberOfClients;
	private int numberOfAccounts;
	private Set<String> clientsSet;
	private float depositSum;
	private float creditSum;
	private Map<String, List<Client>> clientsByCity;

	public static long getSerialVersionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfClients() {
		return numberOfClients;
	}

	public void setNumberOfClients(int numberOfClients) {
		this.numberOfClients = numberOfClients;
	}

	public int getNumberOfAccounts() {
		return numberOfAccounts;
	}

	public void setNumberOfAccounts(int numberOfAccounts) {
		this.numberOfAccounts = numberOfAccounts;
	}

	public Set<String> getClientsSet() {
		return clientsSet;
	}

	public void setClientsSet(Set<String> clientsSet) {
		this.clientsSet = clientsSet;
	}

	public float getDepositSum() {
		return depositSum;
	}

	public void setDepositSum(float depositSum) {
		this.depositSum = depositSum;
	}

	public float getCreditSum() {
		return creditSum;
	}

	public void setCreditSum(float creditSum) {
		this.creditSum = creditSum;
	}

	public Map<String, List<Client>> getClientsByCity() {
		return clientsByCity;
	}

	public void setClientsByCity(Map<String, List<Client>> clientsByCity) {
		this.clientsByCity = clientsByCity;
	}

	@Override
	public String toString() {
		return "BankInfo [name=" + name + ", numberOfClients=" + numberOfClients + ", numberOfAccounts=" + numberOfAccounts + ", clientsSet=" + clientsSet + ", depositSum=" + depositSum + ", creditSum=" + creditSum + "]";
	}
}