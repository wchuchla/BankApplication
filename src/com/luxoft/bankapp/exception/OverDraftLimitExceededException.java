package com.luxoft.bankapp.exception;

public class OverDraftLimitExceededException extends NotEnoughFundsException {
    private static final long serialVersionUID = 1L;
    private final float maxAmount;

    public OverDraftLimitExceededException(float amount, float maxAmount) {
        super(amount);
        this.maxAmount = maxAmount;
    }

    @Override
    public String getMessage() {
        return "There are no enough funds on your account. The overdraft is exceeded by " + amount
                + ". Your maximum amount is " + maxAmount + ".";
    }
}