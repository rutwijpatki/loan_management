package com.fintech.loan.domain;

import java.math.BigDecimal;
import java.util.*;

public class InterviewTraps {

    public static void main(String[] args) {
        trap1StringEquality();
        trap2StringImmutability();
        trap3StringBuilder();
        trap4EqualsHashCode();
        trap5MutableKey();
        trap6IntegerCache();
        trap7BigDecimalEquals();
    }

    static void trap1StringEquality() {
        System.out.println("=== Trap 1: == vs equals() on Strings ===");
        String a = "LOAN";
        String b = "LOAN";              // same object from the String Pool
        String c = new String("LOAN");  // new object on the heap
        System.out.println("a == b          : " + (a == b));
        System.out.println("a == c          : " + (a == c));
        System.out.println("a.equals(c)     : " + a.equals(c));
        System.out.println("a == c.intern() : " + (a == c.intern()));
    }

    static void trap2StringImmutability() {
        System.out.println("\n=== Trap 2: String immutability ===");
        String status = "pending";
        status.toUpperCase();                       // result thrown away!
        System.out.println("Without assigning : " + status);
        status = status.toUpperCase();
        System.out.println("After assigning   : " + status);

        String s1 = "Rutu";
        String s2 = s1;
        s1 = s1 + " Patki";                         // s1 now points to a NEW string
        System.out.println("s1 = " + s1 + " | s2 = " + s2);
    }

    static void trap3StringBuilder() {
        System.out.println("\n=== Trap 3: String vs StringBuilder in a loop ===");
        int n = 50_000;

        long start = System.currentTimeMillis();
        String s = "";
        for (int i = 0; i < n; i++) s += "x";
        long stringMs = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append("x");
        long builderMs = System.currentTimeMillis() - start;

        System.out.println("String +      : " + stringMs + " ms");
        System.out.println("StringBuilder : " + builderMs + " ms");
    }

    static void trap4EqualsHashCode() {
        System.out.println("\n=== Trap 4: equals() + hashCode() ===");
        Set<Customer> set = new HashSet<>();
        set.add(new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F"));
        set.add(new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F"));
        System.out.println("HashSet size with 2 equal customers: " + set.size());

        Map<Customer, Integer> creditScores = new HashMap<>();
        creditScores.put(new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F"), 780);
        Customer lookup = new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F");
        System.out.println("Credit score found with a NEW equal object: " + creditScores.get(lookup));
    }

    static void trap5MutableKey() {
        System.out.println("\n=== Trap 5: Mutable key in a HashSet ===");
        MutableAccount acc = new MutableAccount("ACC-001");
        Set<MutableAccount> accounts = new HashSet<>();
        accounts.add(acc);
        System.out.println("contains before change : " + accounts.contains(acc));
        acc.accountNo = "ACC-999";   // hashCode changes -> HashSet looks in the wrong bucket
        System.out.println("contains after change  : " + accounts.contains(acc));
        System.out.println("but set size is still  : " + accounts.size());
    }

    static void trap6IntegerCache() {
        System.out.println("\n=== Trap 6: Integer cache ===");
        Integer a = 127, b = 127;
        Integer c = 128, d = 128;
        System.out.println("127 == 127      : " + (a == b));
        System.out.println("128 == 128      : " + (c == d));
        System.out.println("128 equals 128  : " + c.equals(d));
    }

    static void trap7BigDecimalEquals() {
        System.out.println("\n=== Trap 7: BigDecimal equals vs compareTo ===");
        BigDecimal x = new BigDecimal("2.0");
        BigDecimal y = new BigDecimal("2.00");
        System.out.println("2.0 equals 2.00      : " + x.equals(y));
        System.out.println("2.0 compareTo 2.00   : " + (x.compareTo(y) == 0));
        Set<BigDecimal> amounts = new HashSet<>(List.of(x, y));
        System.out.println("HashSet size         : " + amounts.size());
    }

    static class MutableAccount {
        String accountNo;
        MutableAccount(String accountNo) { this.accountNo = accountNo; }

        @Override
        public boolean equals(Object o) {
            return o instanceof MutableAccount m && accountNo.equals(m.accountNo);
        }

        @Override
        public int hashCode() { return accountNo.hashCode(); }
    }
}