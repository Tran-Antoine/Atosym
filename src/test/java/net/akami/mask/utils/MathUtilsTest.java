package net.akami.mask.utils;

import net.akami.mask.handler.Divider;
import net.akami.mask.handler.sign.QuaternaryOperationSign.QuaternaryMathOperation;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MathUtilsTest {

    @Test
    public void sumTest() {

        //Assertions.assertThat(MathUtils.sum("2y/3", "5/3")).isEqualTo("2y/3+5/3");
    }

    @Test
    public void multTest() {
        Assertions.assertThat(FormatterFactory.formatForVisual(MathUtils.mult("5","((x)#)"))).isEqualTo("5cos(x)");
        Assertions.assertThat(MathUtils.mult("x^11","x")).isEqualTo("x^12");
        Assertions.assertThat(MathUtils.mult("x^11","x+2")).isEqualTo("x^12+2x^11");
        Assertions.assertThat(MathUtils.mult("x^(y^2)","x^y")).isEqualTo("x^(y^2+y)");
        Assertions.assertThat(MathUtils.mult("x^2+2xy+2xz+y^2+2yz+z^2", "x+y+z")).isEqualTo("x^3+3x^2y+3x^2z+3xy^2+6xyz+3xz^2+y^3+3y^2z+3yz^2+z^3");
        Assertions.assertThat(MathUtils.mult("3","x")).isEqualTo("3x");
        Assertions.assertThat(MathUtils.mult("3x","x")).isEqualTo("3x^2");
        Assertions.assertThat(MathUtils.mult("x^2+2x+1","x+1")).isEqualTo("x^3+3x^2+3x+1");
        Assertions.assertThat(MathUtils.mult("xy^2","x")).isEqualTo("x^2y^2");
        Assertions.assertThat(MathUtils.mult("3x^2y+3xy^2+y^3","x+y")).isEqualTo("3x^3y+6x^2y^2+4xy^3+y^4");
        Assertions.assertThat(MathUtils.mult("2xz+y^2", "x+y")).isEqualTo("2x^2z+2xyz+xy^2+y^3");
        Assertions.assertThat(MathUtils.mult("2xz+y^2", "x+y+z")).isEqualTo("2x^2z+2xyz+2xz^2+xy^2+y^3+y^2z");
    }

    @Test
    public void divideTest() {
        Assertions.assertThat(MathUtils.divide("6.4+6.4z", "3.2")).isEqualTo("2+2z");
        Assertions.assertThat(Divider.getInstance().simpleDivision("-2x", "4")).isEqualTo("x/-2");
    }

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
