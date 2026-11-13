package com.fintech.loan.exception;

import com.fintech.loan.domain.LoanStatus;

public class InvalidLoanStateException extends LoanAppException
{
    public InvalidLoanStateException(Long loanId, LoanStatus currentStatus, String action){
        super("Cannot " + action + " loan with id " + loanId+ "current status is " + currentStatus) ;
    }
}