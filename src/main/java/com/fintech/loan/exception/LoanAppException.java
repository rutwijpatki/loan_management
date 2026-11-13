package com.fintech.loan.exception;

public class LoanAppException extends RuntimeException {
 public LoanAppException(String message) {
         super(message);
     }
     public LoanAppException(String message, Throwable cause) {
     super(message, cause);
 }
}
