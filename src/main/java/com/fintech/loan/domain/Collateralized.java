package com.fintech.loan.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface Collateralized {
    BigDecimal getCollateralValue();   // implementing class must provide
    BigDecimal getPrincipal();         // Loan already has this, so it's satisfied automatically

    // Default method: Loan-to-Value ratio (%) — banks use this to limit risk
    default BigDecimal getLoanToValueRatio() {
        return getPrincipal().multiply(BigDecimal.valueOf(100))
                .divide(getCollateralValue(), 2, RoundingMode.HALF_UP);
    }
}