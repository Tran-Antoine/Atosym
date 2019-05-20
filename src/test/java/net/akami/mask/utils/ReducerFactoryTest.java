package net.akami.mask.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ReducerFactoryTest {

    @Test
    public void singleNumberReduction() {
        assertReduction("5", "5");
        assertReduction("0.445", "0.445");
        assertReduction("-0.045", "-0.045");
        assertReduction("-5", "-5.0");
    }

    @Test
    public void basicNumericOperations() {
        assertReduction("3+5", "8.0");
        assertReduction("3-5", "-2.0");
        assertReduction("-4-5", "-9.0");
        assertReduction("3.2*1.5", "4.8");
        assertReduction("3/4", "0.75");
        assertReduction("3^2", "9.0");
    }

    @Test
    public void multiSpacesTest() {
        assertReduction("4               ", "4");
        assertReduction("5    +         5", "10.0");
    }

    @Test
    public void operationWithPriorityFromLeftToRight() {
        assertReduction("5*2+2", "12.0");
        assertReduction("2*3-2", "4.0");
        assertReduction("2/2*2", "2.0");
    }

    @Test
    public void operationThatNeedsPriorityCheck() {
        assertReduction("5+3*2", "11.0");
        assertReduction("3*2^2", "12.0");
    }

    @Test
    public void longOperationWithMultiSigns() {
        assertReduction("4*6/2-10*18+3", "-165.0");
        assertReduction("8/4+3^2*5^1.3-1000.56", "-925.6305");
    }

    @Test
    public void simpleBracketExpressions() {
        assertReduction("(((((5)))))", "5");
        assertReduction("((((5)*3)*2)*1)", "30.0");
        assertReduction("(5+3)*2", "16.0");
        assertReduction("(3-2)^100", "1.0");
        assertReduction("3/(2+4)", "0.5");
    }

    @Test
    public void basicVariableAdditionAndSubtraction() {
        assertReduction("5x + 2y", "5.0x+2.0y");
        assertReduction("5x+0", "5.0x");
        assertReduction("4x+3x", "7.0x");
        assertReduction("3z-12z", "-9.0z");
    }

    @Test
    public void basicVariableMultiplicationAndDivision() {
        assertReduction("5x*2x", "10.0x^2.0");
        assertReduction("4x*2y", "8.0xy");
        assertReduction("4y*2x", "8.0xy");
        assertReduction("5x/x", "5.0");
        assertReduction("3y^2/y", "3.0y");
    }

    @Test
    public void basicVariablePow() {
        assertReduction("x^y", "x^y");
        assertReduction("x^(x+y)", "x^(x+y)");
    }

    @Test
    public void mixedVariableOperations() {
        assertReduction("4x + 3x * 2y", "4.0x+6.0xy");
        assertReduction("3*((x+2y)*2 - 8z)", "6.0x+12.0y-24.0z");
        assertReduction("(8x+y-3)*(1+2-2)+12^(0+4-3)", "8.0x+y+9.0");
        assertReduction("x^y*x^(y^2)", "x^(y^2.0+y)");
    }

    @Test
    public void poweredBracketsTest() {
        assertReduction("(3+x)^2", "x^2.0+6.0x+9.0");
        assertReduction("(x+y+z)^2", "x^2.0+y^2.0+z^2.0+2.0xy+2.0xz+2.0yz");
        assertReduction("(x+y)^7", "x^7.0+y^7.0+7.0x^6.0y+7.0xy^6.0+21.0x^5.0y^2.0+21.0x^2.0y^5.0+35.0x^4.0y^3.0+35.0x^3.0y^4.0");
        assertReduction("(a+b+c)^5", "a^5.0+b^5.0+c^5.0+5.0a^4.0b+5.0a^4.0c+5.0ab^4.0+5.0b^4.0c+5.0ac^4.0+5.0bc^4.0+10.0a^3.0b^2.0" +
                "+8.0a^3.0bc+10.0a^3.0c^2.0+10.0a^2.0b^3.0+8.0ab^3.0c+10.0b^3.0c^2.0+10.0a^2.0c^3.0+8.0abc^3.0" +
                "+10.0b^2.0c^3.0+12.0a^3.0bc+12.0ab^3.0c+12.0abc^3.0+12.0a^2.0b^2.0c+12.0a^2.0bc^2.0+12.0a^2.0b^2.0c" +
                "+12.0ab^2.0c^2.0+12.0a^2.0bc^2.0+12.0ab^2.0c^2.0+6.0a^2.0b^2.0c+6.0a^2.0bc^2.0+6.0ab^2.0c^2.0");
    }


    // TODO : support for "factorisation", xx + 3x -> x(x+3). It could replace the actual monomialSum ??
    // Like 3x + 5x would give x(3+5) = 8x

    private void assertReduction(String initial, String result) {
       Assertions.assertThat(ReducerFactory.reduce(initial)).isEqualTo(result);
    }
}
