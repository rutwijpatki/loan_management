package com.fintech.loan.domain;

import java.math.BigDecimal;

public class PersonalLoan extends Loan {   // no collateral, so no Collateralized
    private static final int MAX_TENURE_MONTHS = 60;

    public PersonalLoan(Long id, Customer customer, BigDecimal principal,
                        BigDecimal annualInterestRate, int tenureMonths) {
        super(id, customer, principal, annualInterestRate, tenureMonths);
        if (tenureMonths > MAX_TENURE_MONTHS) {
            throw new IllegalArgumentException("Personal loan tenure cannot exceed " + MAX_TENURE_MONTHS + " months");
        }
    }

    @Override
    public String getLoanType() { return "PersonalLoan"; }

    @Override
    public BigDecimal calculateProcessingFee() { return percentOfPrincipal("2"); }

    // Unsecured loan -> stricter rule, then reuse the parent's logic
    @Override
    public void approve() {
        if (getCustomer().getMonthlyIncome().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Personal loan needs verified income");
        }
        super.approve();
    }
}