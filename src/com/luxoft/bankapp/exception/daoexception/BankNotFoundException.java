package com.luxoft.bankapp.exception.daoexception;


public class BankNotFoundException extends DAOException {

    private static final long serialVersionUID = 1L;
    private final String name;

    public BankNotFoundException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Bank " + name + " not found.";
    }
}