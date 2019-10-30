package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import org.junit.Test;

public class DivisionOperatorTest {

    private static final DivisionOperator DIV = MaskContext.DEFAULT.getOperator(DivisionOperator.class);

    @Test
    public void numeric_floating_division() {
        assertOperation("5.0", "2.5", "2.0");
        assertOperation("3", "2.5", "1.2");
    }

    @Test
    public void numeric_fraction_simplification() {
        assertOperation("2", "3", "2.0/3.0");
        assertOperation("4", "6", "2.0/3.0");
        assertOperation("16", "2136", "2.0/267.0");
    }

    @Test
    public void identical_numerator_and_denominator() {
        String expected = "1.0";
        assertOperation("xy", "xy", expected);
        assertOperation("4x^2+6x", "6x+4x^2", expected);
        assertOperation("5-x", "0-1(x-5)", expected);
    }

    @Test
    public void divisions_with_mult_as_numerator_or_denominator() {
        assertOperation("xy", "x", "y");
        assertOperation("xy", "y", "x");
        assertOperation("xyz", "xz", "y");
        assertOperation("abc", "cde", "ab/(de)");
    }

    private void assertOperation(String a, String b, String result) {
        OperatorTestUtils.assertBinaryOperation(a, b, result, DIV);
    }
}
