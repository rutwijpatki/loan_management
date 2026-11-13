package com.fintech.loan.exception;

public class DuplicateCustomerException extends LoanAppException {
    private final String pan;
    public DuplicateCustomerException(String pan) {
        super("Customer with Loan " +pan + " already exists");
        this.pan = pan;
    }
public String getPan() {
        return pan;
}
}