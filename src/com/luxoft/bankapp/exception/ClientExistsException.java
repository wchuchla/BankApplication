package com.luxoft.bankapp.exception;

public class ClientExistsException extends BankException {
    private static final long serialVersionUID = 1L;
    private final String name;

    public ClientExistsException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "There is already client with name " + name + ".";
    }
}