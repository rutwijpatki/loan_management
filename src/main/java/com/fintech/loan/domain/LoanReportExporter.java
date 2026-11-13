package com.fintech.loan.domain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LoanReportExporter {
    public static void exportCsv(List<? extends Loan> loans, Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
      writer.write("id,type,customer,status,principle");
      writer.newLine();
      for (Loan loan : loans) {
          writer.write(loan.getId() + "," + loan.getLoanType() + ","
                  + loan.getCustomer().getName() + "," + loan.getPrincipal() + ","
                  + loan.getStatus());
          writer.newLine();
      }
        }
    }
    public static <T extends Loan> BigDecimal totalPrincipal(List<T> loans) {
        BigDecimal total = BigDecimal.ZERO;
        for (T loan : loans) {
            total = total.add(loan.getPrincipal());
        }
        return total;
    }
//    public static BigDecimal totalBad(List<Loan> loans) {
//        return BigDecimal.ZERO;
//    }
}