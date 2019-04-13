package net.akami.mask.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ReducerFactoryTest {

    @Test
    public void singleNumberReduction() {
        assertReduction("5", "5");
        assertReduction("0.445", "0.445");
        assertReduction("-0.045", "-0.045");
        assertReduction("-5", "-5");
    }

    @Test
    public void basicNumericOperations() {
        assertReduction("3+5", "8");
        assertReduction("3-5", "-2");
        assertReduction("-4-5", "-9");
        assertReduction("3.2*1.5", "4.80");
        assertReduction("3/4", "0.75");
        assertReduction("3^2", "9");
    }

    @Test
    public void multiSpacesTest() {
        assertReduction("4               ", "4");
        assertReduction("5    +         5", "10");
    }

    @Test
    public void operationWithPriorityFromLeftToRight() {
        assertReduction("5*2+2", "12");
        assertReduction("2*3-2", "4");
        assertReduction("2/2*2", "2");
    }

    @Test
    public void operationThatNeedsPriorityCheck() {
        assertReduction("5+3*2", "11");
        assertReduction("3*2^2", "12");
    }

    @Test
    public void longOperationWithMultiSigns() {
        assertReduction("4*6/2-10*18+3", "-165");
        assertReduction("8/4+3^2*5^1.3-1000.56", "-925.630458745729159");
    }

    @Test
    public void simpleBracketExpressions() {
        assertReduction("(((((5)))))", "5");
        assertReduction("((((5)*3)*2)*1)", "30");
        assertReduction("(5+3)*2", "16");
        assertReduction("(3-2)^100", "1");
        assertReduction("3/(2+4)", "0.5");
    }

    @Test
    public void basicVariableAdditionAndSubtraction() {
        assertReduction("5x + 2y", "5x+2y");
        assertReduction("5x+0", "5x");
        assertReduction("4x+3x", "7x");
        assertReduction("3z-12z", "-9z");
    }

    @Test
    public void basicVariableMultiplicationAndDivision() {
        assertReduction("5x*2x", "10x^2");
        assertReduction("4x*2y", "8xy");
        assertReduction("4y*2x", "8xy");
        assertReduction("5x/x", "5");
        assertReduction("3y^2/y", "3y");
    }

    @Test
    public void basicVariablePow() {
        assertReduction("x^y", "x^y");
        assertReduction("x^(x+y)", "x^(x+y)");
    }

    @Test
    public void mixedVariableOperations() {
        assertReduction("4x + 3x * 2y", "4x+6xy");
        assertReduction("3*((x+2y)*2 - 8z)", "6x+12y-24z");
        assertReduction("(8x+y-3)*(1+2-2)+12^(0+4-3)", "8x+y+9");
        assertReduction("x^y*x^(y^2)", "x^(y+y^2)");
    }

    @Test
    public void poweredBracketsTest() {
        assertReduction("(3+x)^2", "9+6x+x^2");
        assertReduction("(x+y+z)^2", "x^2+2xy+2xz+y^2+2yz+z^2");
        assertReduction("(x+y)^7", "x^7+7x^6y+21x^5y^2+35x^4y^3+35x^3y^4+21x^2y^5+7xy^6+y^7");
        assertReduction("(a+b+c)^5", "a^5+5a^4b+5a^4c+10a^3b^2+20a^3bc+10a^3c^2+10a^2b^3+30a^2b^2c" +
                "+30a^2bc^2+10a^2c^3+5ab^4+20ab^3c+30ab^2c^2+20abc^3+5ac^4+b^5+5b^4c+10b^3c^2+10b^2c^3+5bc^4+c^5");
    }


    // TODO : support for "factorisation", xx + 3x -> x(x+3). It could replace the actual monomialSum ??
    // Like 3x + 5x would give x(3+5) = 8x
    // -> method "getCommonPart" instead of roughly checking "are variables similar"

    private void assertReduction(String initial, String result) {
       Assertions.assertThat(ReducerFactory.reduce(initial)).isEqualTo(result);
    }
}
