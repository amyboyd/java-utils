package uk.co.amyboyd.utils;

import java.math.BigDecimal;

/**
 * Money (BigDecimal) utilities.
 */
public class MoneyUtils {
    public static BigDecimal percentOf(BigDecimal fullAmount, int percentToKeep) {
        return fullAmount.divide(new BigDecimal("100.00")).multiply(new BigDecimal(percentToKeep)).setScale(2);
    }

    /**
     * @param amount Must not be null.
     * @return If "amount" is less than or equal to 0, returns 0. Else returns "amount".
     */
    public static BigDecimal dontAllowBelowZero(BigDecimal amount) {
        return (amount.compareTo(BigDecimal.ZERO) == 1 ? amount : BigDecimal.ZERO);
    }
}
