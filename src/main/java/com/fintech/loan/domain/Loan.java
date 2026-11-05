package com.fintech.loan.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class Loan {
    private final Long id;
    private final Customer customer;           // "has-a" relationship
    private final BigDecimal principal;
    private final BigDecimal annualInterestRate; // e.g. 10.5 means 10.5%
    private final int tenureMonths;
    private LoanStatus status;

    protected Loan(Long id, Customer customer, BigDecimal principal,
                BigDecimal annualInterestRate, int tenureMonths) {
        if (principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal must be positive");
        }
        if (tenureMonths <= 0) {
            throw new IllegalArgumentException("Tenure must be positive");
        }
        this.id = id;
        this.customer = customer;
        this.principal = principal;
        this.annualInterestRate = annualInterestRate;
        this.tenureMonths = tenureMonths;
        this.status = LoanStatus.Pending;      // every new loan starts PENDING
    }

    public abstract String getLoanType();
    public abstract BigDecimal calculateProcessingFee();

    public void approve() {
        if (status != LoanStatus.Pending) throw new IllegalStateException("Only PENDING loans can be approved");
        status = LoanStatus.Approved;
    }

    public void reject() {
        if (status != LoanStatus.Pending) throw new IllegalStateException("Only PENDING loans can be rejected");
        status = LoanStatus.Rejected;
    }

    // EMI = P * r * (1+r)^n / ((1+r)^n - 1), where r = monthly rate
    public BigDecimal calculateEmi() {
        MathContext mc = MathContext.DECIMAL64;
        BigDecimal r = annualInterestRate.divide(BigDecimal.valueOf(1200), mc);
        BigDecimal onePlusRPowN = BigDecimal.ONE.add(r).pow(tenureMonths, mc);
        BigDecimal emi = principal.multiply(r, mc).multiply(onePlusRPowN, mc)
                .divide(onePlusRPowN.subtract(BigDecimal.ONE), mc);
        return emi.setScale(2, RoundingMode.HALF_UP);
    }

    protected BigDecimal percentOfPrincipal(String percent) {
        return principal.multiply(new BigDecimal(percent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }


    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public BigDecimal getPrincipal() { return principal; }
    public BigDecimal getAnnualInterestRate() { return annualInterestRate; }
    public int getTenureMonths() { return tenureMonths; }
    public LoanStatus getStatus() { return status; }

    @Override
    public String toString() {
        return getLoanType() + "{id=" + id + ", customer=" + customer.getName() + ", principal=" + principal
                + ", rate=" + annualInterestRate + "%, tenure=" + tenureMonths
                + "m, status=" + status + "}";
    }
}