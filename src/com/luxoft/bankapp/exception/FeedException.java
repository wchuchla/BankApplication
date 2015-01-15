package com.luxoft.bankapp.exception;

public class FeedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FeedException(String message) {
        super(message);
    }
}