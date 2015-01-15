package com.luxoft.bankapp.validator;

import com.luxoft.bankapp.service.BankCommander;

import java.util.Map;

public class Validator {

	private static final String NAME = "name";
	private static final String GENDER = "gender";
	private static final String EMAIL = "email";
	private static final String PHONE_NUMBER = "phonenumber";
	private static final String CITY = "city";
	private static final String INITIAL_OVERDRAFT = "initialoverdraft";
	private static final String ACCOUNTTYPE = "accounttype";
	private static final String OVERDRAFT = "overdraft";
	private static final String BALANCE = "balance";

	private Validator() {

	}

	public static boolean nameValidator(String name) {
		return name.matches("^([A-Z][a-z]+) ([A-Z][a-z]+)$");
	}

	public static boolean emailValidator(String email) {
		return email.matches("[a-z0-9!#$%&'*+=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+=?^_`{|}~-]+)*@(?:[a-z0-9]" +
				"(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
	}

	public static boolean genderValidator(String gender) {
		return gender.matches("^(MALE|FEMALE)$");
	}

	public static boolean phoneNumberValidator(String phoneNumber) {
		return phoneNumber
				.matches("^([\\+]*)([0-9]{2,3})\\-?[-]?([0-9]{3})\\-?[-]?([0-9]{3})[-]\\-?([0-9]{3})$");
	}

	public static boolean cityValidator(String city) {
		return city.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");
	}

	public static boolean isDecimalValidator(String value) {
		return value.matches("^[-]*[0-9]+([,.][0-9]{1,2})?");
	}

	public static boolean isPositiveValidator(String value) {
		return value.matches("^[0-9]+([,.][0-9]{1,2})?");
	}

	public static boolean isMultiplyOf20(String value) {
		return !(isPositiveValidator(value) && (Float.parseFloat(value) % 20 == 0));
	}

	public static boolean accountTypeValidator(String accountType) {
		return accountType.matches("^(Saving|saving|Checking|checking)$");
	}

	public static boolean accountNumberValidator(String accountNumber) {
		return accountNumber.matches("^[0-9]{8,}$");
	}

	public static boolean activeAccountValidator(String activeAccount) {
		int active = Integer.parseInt(activeAccount);
		return ((active < 0 || active > BankCommander.activeClient.getAccounts().size() - 1));
	}

	public static boolean anonymousCommandValidator(String command) {
		return command.matches("^[0|6|7]$");
	}

	public static boolean clientCommandValidator(String command) {
		return command.matches("^[0][0-9]$|^[1][0]$");
	}

	public static boolean noActiveBankCommandValidator(String command) {
		return command.matches("^00$|10$");
	}

	public static boolean noActiveClientCommandValidator(String command) {
		return command.matches("^[0][0-2]$|^09$|^[1][0]$");
	}

	public static boolean dbReportCommandValidator(String command) {
		return command.matches("^[0-6]$");
	}

	public static boolean atmClientCommandValidator(String command) {
		return command.matches("[0-3]");
	}

	public static boolean remoteOfficeCommandValidator(String command) {
		return command.matches("[0-5]");
	}

	public static boolean yesNoValidator(String value) {
		return !value.matches("^(YES|Yes|yes|NO|No|no)$");
	}

	public static boolean feedValidator(Map<String, String> feed) {
		if (feed.containsKey(NAME) && feed.containsKey(GENDER) && feed.containsKey(EMAIL)
				&& feed.containsKey(PHONE_NUMBER) && feed.containsKey(CITY)
				&& feed.containsKey(ACCOUNTTYPE) && feed.containsKey(INITIAL_OVERDRAFT)
				&& feed.containsKey(BALANCE)) {

			if ("Checking".equalsIgnoreCase(feed.get(ACCOUNTTYPE))) {
				if (feed.containsKey(OVERDRAFT)) {
					isPositiveValidator(feed.get(OVERDRAFT));
				} else {
					return false;
				}
			}

			return (nameValidator(feed.get(NAME)) && genderValidator(feed.get(GENDER))
					&& emailValidator(feed.get(EMAIL))
					&& phoneNumberValidator(feed.get(PHONE_NUMBER))
					&& cityValidator(feed.get(CITY))
					&& isPositiveValidator(feed.get(INITIAL_OVERDRAFT))
					&& accountTypeValidator(feed.get(ACCOUNTTYPE))
					&& isPositiveValidator(feed.get(BALANCE)) && isPositiveValidator(feed
					.get(OVERDRAFT)));
		}
		return false;
	}
}