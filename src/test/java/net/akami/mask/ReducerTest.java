package net.akami.mask;

import net.akami.mask.core.MaskExpression;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.Reducer;
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

        Assertions.assertThat(Reducer.reduce(s1).asInt()).isEqualTo(5);
        Assertions.assertThat(Reducer.reduce(s2).asInt()).isEqualTo(15);
        Assertions.assertThat(Reducer.reduce(s3).asExpression()).isEqualTo("12.0");
        Assertions.assertThat(Reducer.reduce(s4).asExpression()).isEqualTo("11.0");
        Assertions.assertThat(Reducer.reduce(s5).asExpression()).isEqualTo("-165.0");
        Assertions.assertThat(Reducer.reduce(s6).asInt()).isEqualTo(2);
        Assertions.assertThat(Reducer.reduce(s7).asExpression()).isEqualTo("undefined");
        Assertions.assertThat(Reducer.reduce(s8).asExpression()).isEqualTo("undefined");
        Assertions.assertThat(Reducer.reduce(s9).asInt()).isEqualTo(343);
        Assertions.assertThat(Reducer.reduce(s10).asExpression()).isEqualTo("12.0");
    }

    @Test
    public void initExpressionVariablesCorrectly() {
        MaskExpression expression = new MaskExpression("xxxy");
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
        MaskExpression expression = new MaskExpression("4x+5-2y");
        Assertions.assertThat(expression.imageFor(5, 5).asInt()).isEqualTo(15);
    }
}
