package net.akami.mask.utils;

import net.akami.mask.handler.Divider;
import net.akami.mask.handler.sign.QuaternaryOperationSign.QuaternaryMathOperation;
import net.akami.mask.operation.MaskContext;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MathUtilsTest {

    /*@Test
    public void sumTest() {
        Assertions.assertThat(MathUtils.sum("3x-2", "x")).isEqualTo("4x-2");
        Assertions.assertThat(MathUtils.sum("((x)#)^2", "")).isEqualTo("((x)#)^2");
        List<String> monomials = Arrays.asList("2xyz", "2xyz", "2xyz");
        Assertions.assertThat(MathUtils.sum(monomials)).isEqualTo("6xyz");
        Assertions.assertThat(MathUtils.sum("(y^2+y)", "0")).isEqualTo("y^2+y");
        Assertions.assertThat(MathUtils.sum("y^2", "y")).isEqualTo("y^2+y");
        Assertions.assertThat(MathUtils.sum("2xyz+2xyz+2xyz", "")).isEqualTo("6xyz");
        Assertions.assertThat(MathUtils.sum("1", "5x^2y")).isEqualTo("1+5x^2y");
        Assertions.assertThat(MathUtils.sum("0.4", "y")).isEqualTo("0.4+y");
        Assertions.assertThat(MathUtils.sum("1/2", "y")).isEqualTo("0.5+y");
        Assertions.assertThat(MathUtils.sum("2/5", "y")).isEqualTo("0.4+y");
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
        MaskContext defaultContext = MaskContext.DEFAULT;

        Assertions.assertThat(defaultContext.getBinaryOperation(Divider.class).simpleDivision("-2x", "4")).isEqualTo("x/-2");
    }*/

    @Test
    public void diffSumTest() {
        QuaternaryMathOperation sum = MathUtils::diffSum;
        assertComputation(sum,"5x", "5", "x^2", "2x","5+2x");
        assertComputation(sum,"-5x", "-5", "3x^2", "6x","-5+6x");
    }

    @Test
    public void diffSubtractTest() {
        QuaternaryMathOperation sub = MathUtils::diffSubtract;
        assertComputation(sub,"5x", "5", "x^2", "2x","5-2x");
        assertComputation(sub,"-5x", "-5", "3x^2", "6x","-5-6x");
    }

    @Test
    public void diffMultTest() {
        QuaternaryMathOperation mult = MathUtils::diffMult;
        assertComputation(mult,"5x", "5", "x^2", "2x","15x^2");
        assertComputation(mult,"-5x", "-5", "3x^2", "6x","-45x^2");
    }

    @Test
    public void diffDivideTest() {
        QuaternaryMathOperation div = MathUtils::diffDivide;
        assertComputation(div,"5x", "5", "x^2", "2x","(-5x^2)/(x^2)^2");
        assertComputation(div,"-5x", "-5", "3x^2", "6x","(15x^2)/(3x^2)^2");
    }

    @Test
    public void diffPowTest() {
        QuaternaryMathOperation pow = MathUtils::diffPow;
        assertComputation(pow,"3x", "3", "3", "0","3*3x^2*3");
        assertComputation(pow,"-5x", "-5", "x+1", "1","(x+1)*(-5x)^x(-5)");
    }

    private void assertComputation(QuaternaryMathOperation op, String a, String aAlt, String b, String bAlt, String r) {
        assertThat(op.compute(a, aAlt, b, bAlt)).isEqualTo(r);
    }
}
