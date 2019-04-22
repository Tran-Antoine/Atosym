package net.akami.mask.utils;

import net.akami.mask.handler.sign.QuaternaryOperationSign.QuaternaryMathOperation;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MathUtilsTest {


    @Test
    public void diffSumTest() {
        QuaternaryMathOperation sum = MathUtils::diffSum;
        Assertions.assertThat(sum.compute("5x", "5", "x^2", "2x")).isEqualTo("5+2x");
        Assertions.assertThat(sum.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("-5+6x");
    }
    @Test
    public void diffSubtractTest() {
        QuaternaryMathOperation sub = MathUtils::diffSubtract;
        Assertions.assertThat(sub.compute("5x", "5", "x^2", "2x")).isEqualTo("5-2x");
        Assertions.assertThat(sub.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("-5-6x");
    }
    @Test
    public void diffMultTest() {
        QuaternaryMathOperation mult = MathUtils::diffMult;
        Assertions.assertThat(mult.compute("5x", "5", "x^2", "2x")).isEqualTo("15x^2");
        Assertions.assertThat(mult.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("-45x^2");
    }
    @Test
    public void diffDivideTest() {
        QuaternaryMathOperation div = MathUtils::diffDivide;
        Assertions.assertThat(div.compute("5x", "5", "x^2", "2x")).isEqualTo("(-5x^2)/(x^2)^2");
        Assertions.assertThat(div.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("(15x^2)/(3x^2)^2");
    }
    @Test
    public void diffPowTest() {
        QuaternaryMathOperation pow = MathUtils::diffPow;
        Assertions.assertThat(pow.compute("3x", "3", "3", "0")).isEqualTo("3*3x^2*3");
        Assertions.assertThat(pow.compute("-5x", "-5", "x+1", "1")).isEqualTo("(x+1)*(-5x)^x(-5)");
    }
}
