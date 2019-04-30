package net.akami.mask.utils;

import net.akami.mask.handler.sign.QuaternaryOperationSign.QuaternaryMathOperation;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class MathUtilsTest {

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
