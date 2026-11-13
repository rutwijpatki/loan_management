package com.fintech.loan.domain;

import com.fintech.loan.exception.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

public class DayVDemo {
    public static void main(String[] args) {
        CustomerRegistry customers = new CustomerRegistry();
        Customer rutu = new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F", new BigDecimal("85000"));
        Customer amit = new Customer(2L, "Amit Shah", "amit@example.com", "PQRSX5678Z");
        customers.register(rutu);
        customers.register(amit);

        LoanRepository repo = new LoanRepository();
        HomeLoan home = new HomeLoan(101L, rutu, new BigDecimal("2500000"), new BigDecimal("8.5"), 240, new BigDecimal("3500000"));
        PersonalLoan personal = new PersonalLoan(102L, amit, new BigDecimal("300000"), new BigDecimal("12"), 36);
        CarLoan car = new CarLoan(103L, rutu, new BigDecimal("800000"), new BigDecimal("9"), 60, new BigDecimal("1000000"));
        repo.save(home);
        repo.save(personal);
        repo.save(car);

        // ---------- 1. Catching specific custom exceptions ----------
        System.out.println("=== 1. Custom exceptions ===");
        try {
            customers.register(new Customer(9L, "Fake Rutu", "fake@x.com", "ABCDE1234F"));
        } catch (DuplicateCustomerException e) {
            System.out.println("DuplicateCustomerException: " + e.getMessage());
        }

        try {
            repo.findByIdOrThrow(999L);
        } catch (LoanNotFoundException e) {
            System.out.println("LoanNotFoundException: " + e.getMessage());
        }

        home.approve();
        try {
            home.reject();   // already approved
        } catch (InvalidLoanStateException e) {
            System.out.println("InvalidLoanStateException: " + e.getMessage());
        }

        try {
            personal.approve();   // Amit has no income
        } catch (LoanEligibilityException e) {
            System.out.println("LoanEligibilityException: " + e.getMessage());
        }

        // ---------- 2. One handler for ALL business errors ----------
        // (Spring's @ControllerAdvice does exactly this on Day 18)
        System.out.println("\n=== 2. Catching the base LoanAppException ===");
        Long[] idsToApprove = {103L, 999L, 101L};
        for (Long id : idsToApprove) {
            try {
                repo.findByIdOrThrow(id).approve();
                System.out.println("Loan " + id + " approved");
            } catch (LoanAppException e) {
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }

        // ---------- 3. finally always runs ----------
        System.out.println("\n=== 3. finally ===");
        System.out.println("Returned: " + finallyDemo());

        // ---------- 4. Generics + checked exception ----------
        System.out.println("\n=== 4. Generics & checked exceptions ===");
        List<HomeLoan> homeLoans = List.of(home);
        System.out.println("Total principal (home loans): " + LoanReportExporter.totalPrincipal(homeLoans));
        System.out.println("Total principal (all loans) : " + LoanReportExporter.totalPrincipal(repo.findAll()));

        try {
            Path file = Path.of("loans-report.csv");
            LoanReportExporter.exportCsv(repo.findAll(), file);
            System.out.println("Report written to " + file.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Could not write report: " + e.getMessage());
        }

        try {
            LoanReportExporter.exportCsv(repo.findAll(), Path.of("no-such-folder/report.csv"));
        } catch (IOException e) {
            System.out.println("IOException caught: " + e.getClass().getSimpleName());
        }
    }

    static String finallyDemo() {
        try {
            return "value from try";
        } finally {
            System.out.println("finally ran BEFORE the method returned");
        }
    }
}