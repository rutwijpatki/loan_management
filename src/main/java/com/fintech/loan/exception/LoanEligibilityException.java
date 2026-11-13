package com.fintech.loan.exception;

public class LoanEligibilityException extends LoanAppException {
    public LoanEligibilityException(String reason) {
        super(reason);
    }
}