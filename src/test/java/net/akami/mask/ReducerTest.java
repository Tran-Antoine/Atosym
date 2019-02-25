package net.akami.mask;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.utils.ReducerFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ReducerTest {

    @Test
    public void singleNumberReduction() {
        String s1 = "5";
        Assertions.assertThat(ReducerFactory.reduce(s1)).isEqualTo("5");
    }
    @Test
    public void basicOperationAndRandomSpaces() {
        String s2 = "5 + 10     ";
        Assertions.assertThat(ReducerFactory.reduce(s2)).isEqualTo("15");
    }
    @Test
    public void operationWithPriorityFromLeftToRight() {
        String s3 = "5*2 + 2";
        Assertions.assertThat(ReducerFactory.reduce(s3)).isEqualTo("12");
    }
    @Test
    public void operationThatNeedsPriorityCheck() {
        String s4 = "5 + 3 * 2";
        Assertions.assertThat(ReducerFactory.reduce(s4)).isEqualTo("11");
    }
    @Test
    public void longOperationWithMultiSigns() {
        String s5 = "4*6/2-10*18+3";
        Assertions.assertThat(ReducerFactory.reduce(s5)).isEqualTo("-165");
    }
    @Test
    public void samePriorityLevelTest() {
        String s6 = "2/2*2";
        Assertions.assertThat(ReducerFactory.reduce(s6)).isEqualTo("2");
    }
    @Test
    public void divisionByZeroOperation() {
        String s7 = "5/0";
        Assertions.assertThat(ReducerFactory.reduce(s7)).isEqualTo("undefined");
    }

    @Test
    public void basicVariableOperation() {
        String s9  = "5x + 2y";
        String s10 = "5x * 2x";
        String s11 = "4x * 2y";
        String s12 = "4x + 3x";
        String s13 = "4x + 3x * 2y";

        Assertions.assertThat(ReducerFactory.reduce(s9)).isEqualTo("5x+2y");
        Assertions.assertThat(ReducerFactory.reduce(s10)).isEqualTo("10x^2");
        Assertions.assertThat(ReducerFactory.reduce(s11)).isEqualTo("8xy");
        Assertions.assertThat(ReducerFactory.reduce(s12)).isEqualTo("7x");
        Assertions.assertThat(ReducerFactory.reduce(s13)).isEqualTo("4x+6xy");
    }


    @Test
    public void initExpressionVariablesCorrectly() {
        MaskExpression expression = new MaskExpression("xxxy");
        Assertions.assertThat(expression.getVariablesAmount()).isEqualTo(2);
    }

    @Test
    public void complexSimplificationTest() {
        String s14 = "3*((x+2y)*2 - 8z)";
        String s15 = "(((((5)))))";
        String s16 = "((((5)*3)*2)*1)";
        String s17 = "(8x+y-3)*(1+2-2)+12^(0+4-3)";
        Assertions.assertThat(ReducerFactory.reduce(s14)).isEqualTo("6x+12y-24z");
        Assertions.assertThat(ReducerFactory.reduce(s15)).isEqualTo("5");
        Assertions.assertThat(ReducerFactory.reduce(s16)).isEqualTo("30");
        Assertions.assertThat(ReducerFactory.reduce(s17)).isEqualTo("8x+y+9");
    }

    @Test
    public void multiPowTest() {
        String s18 = "x^y*x^(y^2)+3";
        Assertions.assertThat(ReducerFactory.reduce(s18)).isEqualTo("x^(y+y^2)+3");
    }

    @Test
    public void negativeStartTest() {
        String s19 = "-6-1";
        Assertions.assertThat(ReducerFactory.reduce(s19)).isEqualTo("-7");
    }

    @Test
    public void poweredBracketsTest() {
        String s20 = "(3+x)^2";
        String s21 = "(x+y+z+3)^5";

        Assertions.assertThat(ReducerFactory.reduce(s20)).isEqualTo("9+6x+x^2");
    }
    // TODO : support for "factorisation", xx + 3x -> x(x+3). It could replace the actual sum ?
    // Like 3x + 5x would give x(3+5) = 8x
    // -> method "getCommonPart" instead of roughly checking "are variables similar"
}
