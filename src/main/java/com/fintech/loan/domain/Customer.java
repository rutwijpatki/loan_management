package com.fintech.loan.domain;

import java.math.BigDecimal;

public class Customer {
    private final Long id;            // final: id never changes
    private final String name;
    private String email;
    private final String pan;
    private BigDecimal monthlyIncome; // BigDecimal for money, never double

    // Full constructor
    public Customer(Long id, String name, String email, String pan, BigDecimal monthlyIncome) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (pan == null || !pan.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
            throw new IllegalArgumentException("Invalid PAN: " + pan);
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.pan = pan;
        this.monthlyIncome = monthlyIncome;
    }

    // Overloaded constructor -> chains to the full one using this(...)
    public Customer(Long id, String name, String email, String pan) {
        this(id, name, email, pan, BigDecimal.ZERO);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPan() { return pan; }
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }

    public void setEmail(String email) { this.email = email; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name='" + name + "', pan='" + pan
                + "', monthlyIncome=" + monthlyIncome + "}";
    }
}