package com.luxoft.bankapp.exception;

public class ClientNotExistsException extends BankException {
    private static final long serialVersionUID = 1L;
    private final String name;

    public ClientNotExistsException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Client " + name + " not found.";
    }
}