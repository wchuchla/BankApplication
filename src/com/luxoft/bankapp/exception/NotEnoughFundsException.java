package com.luxoft.bankapp.exception;

public class NotEnoughFundsException extends BankException {
    private static final long serialVersionUID = 1L;
    protected final float amount;

    public NotEnoughFundsException(float amount) {
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return "There are no enough funds on your account. You exceeded your balance by " + amount
                + ".";
    }
}