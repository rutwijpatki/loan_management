package com.fintech.loan.domain;

import com.fintech.loan.exception.LoanNotFoundException;

import java.math.BigDecimal;
import java.util.*;

public class LoanRepository implements Repository<Loan, Long> {

    private final List<Loan> loans = new ArrayList<>();
    private final Map<Long, Loan> loansById = new HashMap<>();

    @Override
    public void save(Loan loan) {
        if (loansById.containsKey(loan.getId())) {
            throw new IllegalArgumentException("Loan id " + loan.getId() + " already exists");
        }
        loans.add(loan);
        loansById.put(loan.getId(), loan);
    }

    @Override
    public Loan findybyID(Long aLong) {
        return null;
    }

    @Override
    public List<Loan> findall() {
        return List.of();
    }

    @Override
    public Loan findById(Long id) {
        return loansById.get(id);
    }

    public Loan findByIdOrThrow(Long id) {
        Loan loan = loansById.get(id);
        if (loan == null) {
            throw new LoanNotFoundException(id);
        }
        return loan;
    }
    // =====================================================

    @Override
    public List<Loan> findAll() {
        return Collections.unmodifiableList(loans);
    }

    @Override
    public int count() {
        return loans.size();
    }

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
        Map<String, Integer> counts = new TreeMap<>();
        for (Loan loan : loans) {
            String type = loan.getLoanType();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        return counts;
    }

    public Map<String, BigDecimal> totalPrincipalByCustomer() {
        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        for (Loan loan : loans) {
            String name = loan.getCustomer().getName();
            BigDecimal current = totals.getOrDefault(name, BigDecimal.ZERO);
            totals.put(name, current.add(loan.getPrincipal()));
        }
        return totals;
    }

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
                it.remove();
                loansById.remove(loan.getId());
                removed++;
            }
        }
        return removed;
    }
}