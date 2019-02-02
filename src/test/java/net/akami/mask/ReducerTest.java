package net.akami.mask;

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
        String s8 = "99999999999999999999999999999999999999999999999+1";
        String s9 = "7^3";
        String s10 = "3*2^2";

        Assertions.assertThat(Reducer.reduce(s1)).isEqualTo("5");
        Assertions.assertThat(Reducer.reduce(s2)).isEqualTo("15.0");
        Assertions.assertThat(Reducer.reduce(s3)).isEqualTo("12.0");
        Assertions.assertThat(Reducer.reduce(s4)).isEqualTo("11.0");
        Assertions.assertThat(Reducer.reduce(s5)).isEqualTo("-165.0");
        Assertions.assertThat(Reducer.reduce(s6)).isEqualTo("2.0");
        Assertions.assertThat(Reducer.reduce(s7)).isEqualTo("undefined");
        Assertions.assertThat(Reducer.reduce(s8)).isEqualTo("undefined");
        Assertions.assertThat(Reducer.reduce(s9)).isEqualTo("343.0");
        Assertions.assertThat(Reducer.reduce(s10)).isEqualTo("12.0");
    }

    @Test
    public void initExpressionVariablesCorrectly() {
        MathExpression expression = new MathExpression("xxxy");
        Assertions.assertThat(expression.getVariablesAmount()).isEqualTo(2);
    }
    @Test
    public void utilMethodsWork() {
        Assertions.assertThat(MathUtils.sum(0.31111f, 0.2f)).isEqualTo(0.51111f);
        Assertions.assertThat(MathUtils.mult(0.35f, 0.46f)).isEqualTo(0.161f);
        Assertions.assertThat(MathUtils.subtract(0.31111f, 0.2f)).isEqualTo(0.11111f);
    }
    @Test
    public void mathExpressionImageFor() {
        MathExpression expression = new MathExpression("4x+5-2x");
        Assertions.assertThat(expression.imageFor(5).asInt()).isEqualTo(15);
    }
}
