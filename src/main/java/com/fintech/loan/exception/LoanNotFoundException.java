package com.fintech.loan.exception;

public class LoanNotFoundException extends LoanAppException {
    public LoanNotFoundException(Long loanId) {
super("Loan not found with id " + loanId);
    }
}