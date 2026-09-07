package dev.qwe664.bbc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyAmountValidatorTest {

    @Test
    void rejectsNonFiniteAmounts() {
        assertFalse(MoneyAmountValidator.isFinite(Double.NaN));
        assertFalse(MoneyAmountValidator.isFinite(Double.POSITIVE_INFINITY));
        assertFalse(MoneyAmountValidator.isFinite(Double.NEGATIVE_INFINITY));
    }

    @Test
    void acceptsFiniteAmounts() {
        assertTrue(MoneyAmountValidator.isFinite(1.0));
        assertTrue(MoneyAmountValidator.isFinite(Double.MAX_VALUE));
    }

    @Test
    void requiresStrictlyPositiveAmount() {
        assertFalse(MoneyAmountValidator.isPositive(0.0));
        assertFalse(MoneyAmountValidator.isPositive(-1.0));
        assertTrue(MoneyAmountValidator.isPositive(0.01));
    }
}
