package com.fintech.loan.domain;

import java.math.BigDecimal;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Customer rutu = new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F",
                new BigDecimal("85000"));
        Customer amit = new Customer(2L, "Amit Shah", "amit@example.com", "PQRSX5678Z"); // income = 0

        // Polymorphism: a List<Loan> holds different loan types
        List<Loan> loans = List.of(
                new HomeLoan(101L, rutu, new BigDecimal("2500000"), new BigDecimal("8.5"), 240,
                        new BigDecimal("3500000")),
                new PersonalLoan(102L, amit, new BigDecimal("300000"), new BigDecimal("12"), 36),
                new CarLoan(103L, rutu, new BigDecimal("800000"), new BigDecimal("9"), 60,
                        new BigDecimal("1000000"))
        );

        BigDecimal totalFees = BigDecimal.ZERO;
        for (Loan loan : loans) {
            System.out.println(loan);   // toString() calls each subclass's getLoanType()
            System.out.println("   EMI = " + loan.calculateEmi()
                    + ", Processing fee = " + loan.calculateProcessingFee()); // runtime polymorphism

            if (loan instanceof Collateralized c) {   // only Home and Car loans
                System.out.println("   Collateral = " + c.getCollateralValue()
                        + ", LTV = " + c.getLoanToValueRatio() + "%");
            }
            totalFees = totalFees.add(loan.calculateProcessingFee());
        }
        System.out.println("Total processing fees: " + totalFees);

        // PersonalLoan's overridden approve() blocks Amit (no income)
        for (Loan loan : loans) {
            try {
                loan.approve();
                System.out.println(loan.getLoanType() + " " + loan.getId() + " -> " + loan.getStatus());
            } catch (IllegalStateException e) {
                System.out.println(loan.getLoanType() + " " + loan.getId() + " -> blocked: " + e.getMessage());
            }
        }


    }
}