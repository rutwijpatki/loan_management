package com.fintech.loan.domain;

import java.math.BigDecimal;

public class HomeLoan extends Loan implements Collateralized {
    private static final int MAX_TENURE_MONTHS = 360;          // 30 years
    private static final BigDecimal MAX_LTV = new BigDecimal("80");

    private final BigDecimal propertyValue;

    public HomeLoan(Long id, Customer customer, BigDecimal principal,
                    BigDecimal annualInterestRate, int tenureMonths, BigDecimal propertyValue) {
        super(id, customer, principal, annualInterestRate, tenureMonths); // must be first line
        this.propertyValue = propertyValue;
        if (tenureMonths > MAX_TENURE_MONTHS) {
            throw new IllegalArgumentException("Home loan tenure cannot exceed " + MAX_TENURE_MONTHS + " months");
        }
        if (getLoanToValueRatio().compareTo(MAX_LTV) > 0) {
            throw new IllegalArgumentException("Home loan LTV cannot exceed 80%, was " + getLoanToValueRatio() + "%");
        }
    }

    @Override
    public String getLoanType() { return "HomeLoan"; }

    @Override
    public BigDecimal calculateProcessingFee() { return percentOfPrincipal("0.5"); }

    @Override
    public BigDecimal getCollateralValue() { return propertyValue; }
}