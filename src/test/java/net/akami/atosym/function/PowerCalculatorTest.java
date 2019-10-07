package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;
import org.junit.Test;

public class PowerCalculatorTest {

    PowerOperator POW = MaskContext.DEFAULT.getBinaryOperator(PowerOperator.class);

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
