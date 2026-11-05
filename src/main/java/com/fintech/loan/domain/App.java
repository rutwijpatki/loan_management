package com.fintech.loan.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) {

        CustomerRegistry customers = new CustomerRegistry();
        Customer rutu  = new Customer(1L, "Rutu Patki", "rutu@example.com",  "ABCDE1234F", new BigDecimal("85000"));
        Customer amit  = new Customer(2L, "Amit Shah",  "amit@example.com",  "PQRSX5678Z");
        Customer priya = new Customer(3L, "Priya Nair", "priya@example.com", "LMNOP4321Q", new BigDecimal("120000"));
        customers.register(rutu);
        customers.register(amit);
        customers.register(priya);
        System.out.println("Customers registered: " + customers.count());

        LoanRepository repo = new LoanRepository();
        repo.save(new HomeLoan(101L, rutu, bd("2500000"), bd("8.5"), 240, bd("3500000")));
        repo.save(new PersonalLoan(102L, amit, bd("300000"), bd("12"), 36));
        repo.save(new CarLoan(103L, rutu, bd("800000"), bd("9"), 60, bd("1000000")));
        repo.save(new PersonalLoan(104L, priya, bd("500000"), bd("11"), 48));
        repo.save(new HomeLoan(105L, priya, bd("4000000"), bd("8.75"), 300, bd("6000000")));
        repo.save(new CarLoan(106L, amit, bd("600000"), bd("9.5"), 60, bd("700000")));
        System.out.println("Loans saved: " + repo.count());

        repo.findById(101L).approve();
        repo.findById(103L).approve();
        repo.findById(105L).approve();
        repo.findById(102L).reject();
        repo.findById(106L).reject();

        System.out.println("\nFind by id 103: " + repo.findById(103L));
        System.out.println("Find by id 999: " + repo.findById(999L));   // null

        System.out.print("\nApproved loans: ");
        printIds(repo.findByStatus(LoanStatus.Approved));

        System.out.print("Rutu's loans: ");
        printIds(repo.findByCustomerPan("ABCDE1234F"));

        System.out.println("\nGrouped by status:");
        for (Map.Entry<LoanStatus, List<Loan>> entry : repo.groupByStatus().entrySet()) {
            System.out.print("  " + entry.getKey() + " -> ");
            printIds(entry.getValue());
        }

        System.out.println("\nCount by type: " + repo.countByType());
        System.out.println("Total principal by customer: " + repo.totalPrincipalByCustomer());

        System.out.print("\nSorted by principal (high to low): ");
        printIds(repo.sortedByPrincipalDesc());

        int removed = repo.removeRejected();
        System.out.println("\nRemoved " + removed + " rejected loans. Remaining: " + repo.count());

        try {
            customers.register(new Customer(4L, "Fake Rutu", "x@y.com", "ABCDE1234F"));
        } catch (IllegalArgumentException e) {
            System.out.println("\nBlocked: " + e.getMessage());
        }

        Set<Customer> set = new HashSet<>();
        set.add(new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F"));
       // set.add(new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F"));
        System.out.println("\nHashSet size with 2 identical customers: " + set.size() + "  <-- why not 1? (Day 4!)");
    }

    private static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    private static void printIds(List<Loan> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getId()).append(" ").append(list.get(i).getLoanType());
            if (i < list.size() - 1) sb.append(", ");
        }
        System.out.println(sb.append("]"));
    }
}