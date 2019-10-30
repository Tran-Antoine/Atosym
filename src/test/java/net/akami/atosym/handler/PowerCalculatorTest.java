package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.operator.ExponentiationOperator;
import org.junit.Test;

public class PowerCalculatorTest {

    ExponentiationOperator POW = MaskContext.DEFAULT.getOperator(ExponentiationOperator.class);

    @Test
    public void powTest() {
        assertPow("4", "1/2","2");
        assertPow("4", "2", "16");
        assertPow("3x", "2", "9x^2");
    }

    private void assertPow(String a, String b, String result) {
        //assertThat(POW.rawOperate(a, b)).isEqualTo(merge);
    }

}
