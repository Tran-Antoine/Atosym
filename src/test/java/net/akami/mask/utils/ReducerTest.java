package net.akami.mask.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReducerTest {

    @Test
    public void singleNumberReduction() {
        String s1 = "5";
        assertReduction(s1, "5");
    }

    @Test
    public void basicOperationAndRandomSpaces() {
        String s2 = "5 + 10     ";
        assertReduction(s2, "15.0");
    }
    @Test
    public void operationWithPriorityFromLeftToRight() {
        String s3 = "5*2 + 2";
        String s3b = "0 + 4";
        assertReduction(s3, "12.0");
        assertReduction(s3b, "4.0");
    }
    @Test
    public void operationThatNeedsPriorityCheck() {
        String s4 = "5 + 3 * 2";
        assertReduction(s4, "11.0");
    }
    @Test
    public void longOperationWithMultiSigns() {
        String s5 = "4*6/2-10*18+3";
        assertReduction(s5, "-165.0");
    }
    @Test
    public void samePriorityLevelTest() {
        String s6 = "2/2*2";
        assertReduction(s6, "2.0");
    }

    @Test
    public void basicVariableOperation() {
        String s9  = "5x + 2y";
        String s9b = "5x + 0";
        String s10 = "5x * 2x";
        String s11 = "4x * 2y";
        String s12 = "4x + 3x";
        String s13 = "4x + 3x * 2y";
        assertReduction(s9, "5.0x+2.0y");
        assertReduction(s9b, "5.0x");
        assertReduction(s10, "10.0x^2.0");
        assertReduction(s11, "8.0xy");
        assertReduction(s12, "7.0x");
        assertReduction(s13, "4.0x+6.0xy");
    }


    @Test
    public void complexSimplificationTest() {
        String s14 = "3*((x+2y)*2 - 8z)";
        String s15 = "(((((5)))))";
        String s16 = "((((5)*3)*2)*1)";
        String s17 = "(8x+y-3)*(1+2-2)+12^(0+4-3)";
        assertReduction(s14, "6.0x+12.0y-24.0z");
        assertReduction(s15, "5");
        assertReduction(s16, "30.0");
        assertReduction(s17, "8.0x+y+9.0");
    }

    @Test
    public void multiPowTest() {
        String s18 = "x^y*x^(y^2)";
        assertReduction(s18, "x^(y^2.0+y)");
    }

    @Test
    public void negativeStartTest() {
        String s19 = "-6-1";
        assertReduction(s19, "-7.0");
    }

    // TODO : Fix, s21 is wrong !
    @Test
    public void poweredBracketsTest() {
        String s20 = "(3+x)^2";
        String s21 = "(x+y+z)^2";
        assertReduction(s20, "x^2.0+6.0x+9.0");
        assertReduction(s21, "x^2.0+y^2.0+z^2.0+2.0xy+2.0xz+2.0yz");

    }

    private void assertReduction(String s1, String s) {
        assertThat(ReducerFactory.reduce(s1).toString()).isEqualTo(s);
    }

    // It won't support factorisation for now. Therefore :
    // (x^2 + 2x + 1) / (x+1) won't give (x+1)
    /*@Test
    public void decomposeExpressionTest() {
        Divider div = MaskContext.DEFAULT.getBinaryOperation(Divider.class);
        Assertions.assertThat(div.simpleDivision("4", "2")).isEqualTo("2");
        Assertions.assertThat(div.simpleDivision("5", "2")).isEqualTo("5/2");
        Assertions.assertThat(div.simpleDivision("6", "4")).isEqualTo("3/2");
        Assertions.assertThat(div.simpleDivision("18", "16")).isEqualTo("9/8");

        StringBuilder builder = new StringBuilder();
        MathUtils.decomposeNumberToString(18).forEach(x -> builder.append(x).append("*"));
        builder.deleteCharAt(builder.getElementsSize()-1);
        Assertions.assertThat(builder.toString()).isEqualTo("2*3*3");
    }*/

    /*@Test
    public void divisionsTest() {
        Assertions.assertThat(MathUtils.divide("5+6", "3")).isEqualTo("5/3+2");
        Assertions.assertThat(MathUtils.divide("6+x", "2")).isEqualTo("3+x/2");
        Assertions.assertThat(MathUtils.divide("2x", "x")).isEqualTo("2");
        Assertions.assertThat(MathUtils.divide("2x+3", "x")).isEqualTo("2+3/x");
        Assertions.assertThat(MathUtils.mult("3", "x/3")).isEqualTo("x");
    }*/
    // TODO : support for "factorisation", xx + 3x -> x(x+3). It could replace the actual monomialSum ??
    // Like 3x + 5x would give x(3+5) = 8x
    // -> method "getCommonPart" instead of roughly checking "are variables similar"
}
