package com.luxoft.bankapp.exception;

public class AccountExistsException extends BankException {
    private static final long serialVersionUID = 1L;
    private final String type;

    public AccountExistsException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return "Client already has " + type + " account.";
    }
}