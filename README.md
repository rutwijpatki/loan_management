# FinTech Loan Management System

A full-stack loan management application for customers, loan officers and admins.
Customers apply for loans, officers review and approve/reject them, and the system
calculates EMIs and tracks application status.

> 🚧 Work in progress — built as part of a 60-day Java full-stack learning plan (Nov–Dec 2026).

## Tech Stack

| Layer | Technology | Status |
|---|---|---|
| Language | Java 21 | ✅ In progress |
| Database | Oracle SQL | ⏳ Planned |
| Backend | Spring Boot, REST, JPA/Hibernate | ⏳ Planned |
| Security | Spring Security + JWT | ⏳ Planned |
| Frontend | Angular + TypeScript | ⏳ Planned |
| DevOps | Docker, Docker Compose | ⏳ Planned |

## Features

- [x] Customer and Loan domain model with validation (PAN format, positive amounts)
- [x] Loan status lifecycle: PENDING → APPROVED / REJECTED
- [x] EMI calculation using BigDecimal for precise money handling
- [x] Loan type hierarchy: abstract Loan → HomeLoan, PersonalLoan, CarLoan
- [x] Collateralized interface with Loan-to-Value (LTV) calculation
- [x] Type-specific rules: max tenure, processing fee, LTV limits
- [ ] Oracle database schema
- [ ] REST API for customers, loan products and applications
- [ ] JWT login with roles: CUSTOMER, LOAN_OFFICER, ADMIN
- [ ] Angular dashboard and loan application form
- [ ] Dockerized setup

## Project Structure

```
src/main/java/com/fintech/loan/domain/
├── Customer.java        # Customer entity with PAN validation
├── Loan.java            # Abstract base: EMI, status transitions
├── HomeLoan.java        # Secured, max 360 months, LTV ≤ 80%
├── CarLoan.java         # Secured, max 84 months, LTV ≤ 90%
├── PersonalLoan.java    # Unsecured, max 60 months, income check
├── Collateralized.java  # Interface for asset-backed loans (LTV)
├── LoanStatus.java      # PENDING, APPROVED, REJECTED
└── App.java             # Demo runner
```

## How to Run

Requirements: JDK 21, Maven

```
git clone https://github.com/YOUR-USERNAME/fintech-loan-management.git
cd fintech-loan-management
```

Open in IntelliJ IDEA and run `App.java`.

## Design Decisions

- **BigDecimal for money:** avoids floating-point errors (0.1 + 0.2 ≠ 0.3 with double).
- **Status changes through methods** (`approve()`, `reject()`), not setters, so invalid
  transitions like approving a rejected loan are blocked.
- **Abstract class for shared loan logic, interface for capabilities:** every loan *is a* Loan,
  but only secured loans *can be* Collateralized. New loan types need just one new class.
- **PersonalLoan does not implement Collateralized:** it is unsecured, so it uses an income
  check instead of LTV (Interface Segregation Principle).

## Author

Rutu Patki · rutupatki25@gmail.com