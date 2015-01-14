package com.luxoft.bankapp.model;

import com.luxoft.bankapp.exception.FeedException;
import com.luxoft.bankapp.model.enums.Gender;
import com.luxoft.bankapp.model.info.ClientInfo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class Client implements Report, Serializable, Comparable<Client> {
	private static final long serialVersionUID = 1L;
	private static final String ACCOUNT_TYPE = "accounttype";
	private static final String INITIAL_OVERDRAFT = "initialoverdraft";
	private static final String CITY = "city";
	private static final String PHONE_NUMBER = "phonenumber";
	private static final String EMAIL = "email";
	private static final String GENDER = "gender";
	private final Set<Account> accounts = new TreeSet<>();
	@NoDB
	private int id;
	private String name;
	private Gender gender;
	private String email;
	private String phoneNumber;
	private String city;
	private Account activeAccount;
	private float initialOverdraft;

	/**
	 * Constructs a new client with the specified name, gender, email, phone number, city and initial overdraft
	 *
	 * @param name             the client's name
	 * @param gender           the client's gender
	 * @param email            the client's email
	 * @param phoneNumber      the client's phone number
	 * @param city             the client's city
	 * @param initialOverdraft the client's initial overdraft
	 * @throws java.lang.IllegalArgumentException if the initial overdraft is negative
	 * @throws java.lang.NullPointerException     if the name, gender, email, phone number or city is null
	 */
	public Client(String name, Gender gender, String email, String phoneNumber, String city,
				  float initialOverdraft) {
		if (initialOverdraft >= 0) {
			this.name = name;
			this.gender = gender;
			this.email = email;
			this.phoneNumber = phoneNumber;
			this.city = city;
			this.initialOverdraft = initialOverdraft;
		} else {
			throw new IllegalArgumentException(
					"Initial overdraft must be greater than or equal to 0");
		}
	}

	/**
	 * Constructs a new client with the specified name
	 *
	 * @param name the client's name
	 * @throws java.lang.NullPointerException if the specified name is null
	 */
	public Client(String name) {
		this(name, Gender.MALE, "", "", "", 0.0f);
	}

	/**
	 * Constructs a new client from ClientInfo bean
	 *
	 * @param info the client's info
	 * @throws java.lang.NullPointerException if the specified info is null
	 */
	public Client(ClientInfo info) {
		name = info.getName();
		gender = info.getGender();
		email = info.getEmail();
		phoneNumber = info.getPhoneNumber();
		city = info.getCity();
		initialOverdraft = info.getInitialOverdraft();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void parseFeed(Map<String, String> feed) {
		String clientGender = feed.get(GENDER);
		email = feed.get(EMAIL);
		phoneNumber = feed.get(PHONE_NUMBER);
		city = feed.get(CITY);
		initialOverdraft = Float.parseFloat(feed.get(INITIAL_OVERDRAFT));
		String accountType = feed.get(ACCOUNT_TYPE);

		if ("m".equalsIgnoreCase(clientGender)) {
			this.gender = Gender.MALE;
		} else if ("f".equalsIgnoreCase(clientGender)) {
			this.gender = Gender.FEMALE;
		}

		Account account = getAccountByType(accountType);
		account.parseFeed(feed);
	}

	private Account getAccountByType(String accountType) {
		for (Account account : accounts) {
			if (account.getAccountType().equals(accountType)) {
				return account;
			}
		}
		return createAccountByType(accountType);
	}

	private Account createAccountByType(String accountType) {
		Account account;
		if ("Saving".equalsIgnoreCase(accountType)) {
			account = new SavingAccount();
		} else if ("Checking".equalsIgnoreCase(accountType)) {
			account = new CheckingAccount();
		} else {
			throw new FeedException("Account type not valid " + accountType);
		}
		accounts.add(account);
		activeAccount = account;
		return account;
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

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public Set<Account> getAccounts() {
		return Collections.unmodifiableSet(accounts);
	}

	public Account getActiveAccount() {
		return activeAccount;
	}

	public void setActiveAccount(Account activeAccount) {
		this.activeAccount = activeAccount;
	}

	public float getInitialOverdraft() {
		return initialOverdraft;
	}

	public void setInitialOverdraft(float initialOverdraft) {
		if (initialOverdraft < 0) {
			throw new IllegalArgumentException("Initial overdraft cannot be negative.");
		}
		this.initialOverdraft = initialOverdraft;
	}

	public String getClientSalutation() {
		return gender.getPrefix() + " " + name;
	}

	@Override
	public void printReport() {
		System.out.println("ID: " + id + ", Name: " + getClientSalutation() + ", Gender: " + gender
				+ ", Email: " + email + ", Phone number: " + phoneNumber + ", City: " + city
				+ "\nList of accounts:");
		for (Account account : accounts) {
			account.printReport();
			System.out.println();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Client other = (Client) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (gender != other.gender) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (phoneNumber == null) {
			if (other.phoneNumber != null) {
				return false;
			}
		} else if (!phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Client c) {
		return name.compareTo(c.getName());
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", gender=" + gender + ", email=" + email + ", phoneNumber=" + phoneNumber + ", city=" + city + ", initialOverdraft=" + initialOverdraft + "]";
	}
}