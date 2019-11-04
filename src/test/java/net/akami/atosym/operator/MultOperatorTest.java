package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import org.junit.Test;

public class MultOperatorTest {

    private static final MultOperator MULT = MaskContext.DEFAULT.getOperator(MultOperator.class);

    // n * m = k    with n, m, k : known numbers
    @Test
    public void numeric_mult() {
        assertOperation("3", "4", "12.0");
        assertOperation("1.3", "7.24", "9.412");
    }

    // a * b = ab    with a, b : numbers or variables
    // This property doesn't need to be written, since all non merged elements will be automatically added to
    // the final mult object
    @Test
    public void non_simplifiable_sum() {
        assertOperation("3", "x", "3.0x");
        assertOperation("abc", "d", "abcd");
    }

    @Test
    public void partially_simplifiable_sum() {
        assertOperation("3x", "3y", "9.0xy");
    }

    @Test
    public void partially_simplifiable_sum_with_identical_variables() {
        assertOperation("5xy", "3xz", "15.0x^2.0yz");
    }

    // a * a = a^2    with a : variable
    @Test
    public void variable_multiplied_by_itself() {
        assertOperation("x", "x", "x^2.0");
    }

    @Test
    public void simple_identical_base_mult() {
        assertOperation("x^2", "x^3", "x^5.0");
    }

    private void assertOperation(String a, String b, String result) {
        OperatorTestUtils.assertBinaryOperation(a, b, result, MULT);
    }

        /*@Test
    public void multTest() {
        //assertOperation("3", "x/3","x");
        assertOperation("x^11","x","x^12.0");
        assertOperation("x^11","x+2","x^12.0+2.0x^11.0");
        assertOperation("x^(y^2)","x^y","x^(y^2.0+y)");
        assertOperation("x^2+2xy+2xz+y^2+2yz+z^2", "x+y+z",
                "x^3.0+y^3.0+z^3.0+3.0x^2.0y+3.0x^2.0z+3.0y^2.0x+3.0y^2.0z+3.0z^2.0x+3.0z^2.0y+6.0xyz");
        assertOperation("3","x","3.0x");
        assertOperation("3x","x","3.0x^2.0");
        assertOperation("x^2+2x+1","x+1","x^3.0+3.0x^2.0+3.0x+1.0");
        assertOperation("xy^2","x","x^2.0y^2.0");
        assertOperation("3x^2y+3xy^2+y^3","x+y","y^4.0+3.0x^3.0y+4.0y^3.0x+6.0x^2.0y^2.0");
        assertOperation("2xz+y^2", "x+y","y^3.0+2.0x^2.0z+y^2.0x+2.0xyz");
        assertOperation("2xz+y^2", "x+y+z","y^3.0+2.0x^2.0z+y^2.0x+y^2.0z+2.0z^2.0x+2.0xyz");
    }*/
}
