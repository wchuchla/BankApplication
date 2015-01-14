package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.enums.Gender;

import java.io.Serializable;
import java.util.Set;

public class ClientInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Gender gender;
	private String email;
	private String phoneNumber;
	private String city;
	private Set<Account> accounts;
	private float initialOverdraft;

	public ClientInfo(Client client) {
		name = client.getName();
		gender = client.getGender();
		email = client.getEmail();
		phoneNumber = client.getPhoneNumber();
		city = client.getCity();
		accounts = client.getAccounts();
		initialOverdraft = client.getInitialOverdraft();
	}

	public ClientInfo(String name, Gender gender, String email, String phoneNumber, String city, float initialOverdraft) {
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.city = city;
		this.initialOverdraft = initialOverdraft;
	}

	public static long getSerialVersionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public float getInitialOverdraft() {
		return initialOverdraft;
	}

	public void setInitialOverdraft(float initialOverdraft) {
		this.initialOverdraft = initialOverdraft;
	}

	@Override
	public String toString() {
		return "ClientInfo [name=" + name + ", gender=" + gender + ", email=" + email + ", phoneNumber="
				+ phoneNumber + ", city=" + city + ", accounts=" + accounts + ", initialOverdraft=" + initialOverdraft + "]";
	}
}