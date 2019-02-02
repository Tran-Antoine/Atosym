package net.akami.mask.core;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ReducerTest {

    @Test
    public void reduceEquationFromSequenceTest() {

        String s1 = "5";
        String s2 = "5 + 10     ";
        String s3 = "5*2 + 2";
        String s4 = "5 + 3 * 2";
        String s5 = "4*6/2-10*18+3";
        String s6 = "2/2*2";
        String s7 = "5/0";
        String s8 = "9999999999+1";

        Assertions.assertThat(Reducer.reduce(s1)).isEqualTo("5");
        Assertions.assertThat(Reducer.reduce(s2)).isEqualTo("15");
        Assertions.assertThat(Reducer.reduce(s3)).isEqualTo("12");
        Assertions.assertThat(Reducer.reduce(s4)).isEqualTo("11");
        Assertions.assertThat(Reducer.reduce(s5)).isEqualTo("-165");
        Assertions.assertThat(Reducer.reduce(s6)).isEqualTo("2");
        Assertions.assertThat(Reducer.reduce(s7)).isEqualTo("undefined");
        Assertions.assertThat(Reducer.reduce(s8)).isEqualTo("undefined");
    }
    @Test
    public void initExpressionVariablesCorrectly() {
        MathExpression expression = new MathExpression("xxxy");
        Assertions.assertThat(expression.getVariablesAmount()).isEqualTo(2);
    }
}
