package com.fintech.loan.domain;

import java.math.BigDecimal;

public class CarLoan extends Loan implements Collateralized {
    private static final int MAX_TENURE_MONTHS = 84;           // 7 years
    private static final BigDecimal MAX_LTV = new BigDecimal("90");

    private final BigDecimal vehicleValue;

    public CarLoan(Long id, Customer customer, BigDecimal principal,
                   BigDecimal annualInterestRate, int tenureMonths, BigDecimal vehicleValue) {
        super(id, customer, principal, annualInterestRate, tenureMonths);
        this.vehicleValue = vehicleValue;
        if (tenureMonths > MAX_TENURE_MONTHS) {
            throw new IllegalArgumentException("Car loan tenure cannot exceed " + MAX_TENURE_MONTHS + " months");
        }
        if (getLoanToValueRatio().compareTo(MAX_LTV) > 0) {
            throw new IllegalArgumentException("Car loan LTV cannot exceed 90%");
        }
    }

    @Override
    public String getLoanType() { return "CarLoan"; }

    @Override
    public BigDecimal calculateProcessingFee() { return percentOfPrincipal("1"); }

    @Override
    public BigDecimal getCollateralValue() { return vehicleValue; }
}