package com.fintech.loan.domain;

import java.math.BigDecimal;
import java.util.*;

public class LoanRepository {
    private final List<Loan> loans = new ArrayList<>();
    private final Map<Long, Loan> loansById = new HashMap<>();

    public void save(Loan loan) {
        if (loansById.containsKey(loan.getId())) {
            throw new IllegalArgumentException("Loan id " + loan.getId() + " already exists");
        }
        loans.add(loan);
        loansById.put(loan.getId(), loan);
    }

    public Loan findById(Long id) {
        return loansById.get(id);
    }

    public List<Loan> findAll() {
        return Collections.unmodifiableList(loans);
    }

    // ---------- Search (loops for now; Streams on Day 6) ----------
    public List<Loan> findByStatus(LoanStatus status) {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getStatus() == status) result.add(loan);
        }
        return result;
    }

    public List<Loan> findByCustomerPan(String pan) {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getCustomer().getPan().equals(pan)) result.add(loan);
        }
        return result;
    }

    public Map<LoanStatus, List<Loan>> groupByStatus() {
        Map<LoanStatus, List<Loan>> groups = new EnumMap<>(LoanStatus.class);
        for (Loan loan : loans) {
            if (!groups.containsKey(loan.getStatus())) {
                groups.put(loan.getStatus(), new ArrayList<>());
            }
            groups.get(loan.getStatus()).add(loan);
        }
        return groups;
    }

    public Map<String, Integer> countByType() {
        Map<String, Integer> counts = new TreeMap<>();   // keys sorted A→Z
        for (Loan loan : loans) {
            String type = loan.getLoanType();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        return counts;
    }

    public Map<String, BigDecimal> totalPrincipalByCustomer() {
        Map<String, BigDecimal> totals = new LinkedHashMap<>();   // keeps insertion order
        for (Loan loan : loans) {
            String name = loan.getCustomer().getName();
            BigDecimal current = totals.getOrDefault(name, BigDecimal.ZERO);
            totals.put(name, current.add(loan.getPrincipal()));
        }
        return totals;
    }

    // ---------- Sorting ----------
    public List<Loan> sortedByPrincipalDesc() {
        List<Loan> copy = new ArrayList<>(loans);
        copy.sort(Comparator.comparing(Loan::getPrincipal).reversed());
        return copy;
    }

    public int removeRejected() {
        int removed = 0;
        Iterator<Loan> it = loans.iterator();
        while (it.hasNext()) {
            Loan loan = it.next();
            if (loan.getStatus() == LoanStatus.Rejected) {
                it.remove();                       // safe
                loansById.remove(loan.getId());    // keep both structures in sync
                removed++;
            }
        }
        return removed;
    }

    public int count() {
        return loans.size();
    }
}