package com.luxoft.bankapp.exception;

public class AccountNotExistsException extends BankException {
    private static final long serialVersionUID = 1L;
    private final String type;

    public AccountNotExistsException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return "You have got no " + type + " account.";
    }
}