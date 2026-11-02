package com.fintech.loan.domain;

import com.fintech.loan.domain.*;
import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {
        Customer rutu = new Customer(1L, "Rutu Patki", "rutu@example.com", "ABCDE1234F",
                new BigDecimal("85000"));
        Customer amit = new Customer(2L, "Amit Shah", "amit@example.com", "PQRSX5678Z"); // chained constructor

        Loan home = new Loan(101L, rutu, new BigDecimal("2500000"), new BigDecimal("8.5"), 240);
        Loan personal = new Loan(102L, amit, new BigDecimal("300000"), new BigDecimal("12"), 36);

        System.out.println(rutu);
        System.out.println(amit);
        System.out.println(home + " EMI=" + home.calculateEmi());
        System.out.println(personal + " EMI=" + personal.calculateEmi());

        home.approve();
        personal.reject();
        System.out.println("After review: " + home.getStatus() + ", " + personal.getStatus());

        // Try these one at a time, then comment them out. Read each exception message:
        // new Customer(3L, "", "x@y.com", "ABCDE1234F");   // blank name
        // new Customer(4L, "Test", "x@y.com", "123");      // bad PAN
        // home.reject();                                   // already APPROVED
    }
}