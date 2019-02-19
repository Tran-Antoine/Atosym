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
    public void mathExpressionImageFor() {
        MaskExpression expression = new MaskExpression("4x+5-2y");
        //Assertions.assertThat(expression.imageFor(5, 5).asInt()).isEqualTo(15);
    }
}
