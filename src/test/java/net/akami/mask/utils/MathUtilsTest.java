package net.akami.mask.utils;

import net.akami.mask.operation.Division;
import net.akami.mask.operation.sign.QuaternaryOperationSign;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.operation.sign.QuaternaryOperationSign.QuaternaryMathOperation;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void sumTest() {

        Assertions.assertThat(MathUtils.sum("0.4", "y")).isEqualTo("0.4+y");
        Assertions.assertThat(MathUtils.sum("1/2", "y")).isEqualTo("0.5+y");
        Assertions.assertThat(MathUtils.sum("2/5", "y")).isEqualTo("0.4+y");
        //Assertions.assertThat(MathUtils.sum("2y/3", "5/3")).isEqualTo("2y/3+5/3");
    }

    @Test
    public void divideTest() {
        Assertions.assertThat(MathUtils.divide("6.4+6.4z", "3.2")).isEqualTo("2+2z");
        Assertions.assertThat(Division.getInstance().simpleDivision("-2x", "4")).isEqualTo("x/-2");
    }

    @Test
    public void diffSumTest() {
        QuaternaryMathOperation sum = MathUtils::diffSum;
        Assertions.assertThat(sum.compute("5x", "5", "x^2", "2x")).isEqualTo("5+2x");
        Assertions.assertThat(sum.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("-5+6x");
    }
    @Test
    public void diffSubtractTest() {
        QuaternaryMathOperation sum = MathUtils::diffSubtract;
        Assertions.assertThat(sum.compute("5x", "5", "x^2", "2x")).isEqualTo("5-2x");
        Assertions.assertThat(sum.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("-5-6x");
    }
    @Test
    public void diffMultTest() {
        QuaternaryMathOperation sum = MathUtils::diffMult;
        Assertions.assertThat(sum.compute("5x", "5", "x^2", "2x")).isEqualTo("15x^2");
        Assertions.assertThat(sum.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("-45x^2");
    }
    @Test
    public void diffDivideTest() {
        QuaternaryMathOperation sum = MathUtils::diffDivide;
        Assertions.assertThat(sum.compute("5x", "5", "x^2", "2x")).isEqualTo("(-5x^2)/(x^2)^2");
        Assertions.assertThat(sum.compute("-5x", "-5", "3x^2", "6x")).isEqualTo("(15x^2)/(3x^2)^2");
    }
    @Test
    public void diffPowTest() {
        QuaternaryMathOperation sum = MathUtils::diffPow;
        Assertions.assertThat(sum.compute("3x", "3", "3", "0")).isEqualTo("(9x)^(2)");
        Assertions.assertThat(sum.compute("-5x", "-5", "x+1", "1")).isEqualTo("(-5x^2-5x)^(x)");
    }
}
