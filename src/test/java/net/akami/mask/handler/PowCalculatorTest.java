package net.akami.mask.handler;

import static org.assertj.core.api.Assertions.assertThat;

import net.akami.mask.operation.MaskContext;
import org.junit.Test;

public class PowCalculatorTest {

    PowCalculator POW = MaskContext.DEFAULT.getBinaryOperation(PowCalculator.class);

    @Test
    public void powTest() {
        assertPow("4", "1/2","2");
        assertPow("4", "2", "16");
        assertPow("3x", "2", "9x^2");
    }

    private void assertPow(String a, String b, String result) {
        //assertThat(POW.rawOperate(a, b)).isEqualTo(result);
    }

}
