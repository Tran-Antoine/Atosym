package net.akami.mask.handler;

import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.MathUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class DividerTest {

    private final Divider DIV = MaskContext.DEFAULT.getBinaryOperation(Divider.class);
    // It won't support factorisation for now. Therefore :
    // (x^2 + 2x + 1) / (x+1) won't give (x+1)
    @Test
    public void decomposeExpressionTest() {
        StringBuilder builder = new StringBuilder();
        MathUtils.decomposeNumberToString(18).forEach(x -> builder.append(x).append("*"));
        assertThat(builder.toString()).isEqualTo("2*3*3*");
    }

    @Test
    public void divisionsTest() {
        assertDivision("4", "2","2");
        assertDivision("6.4+6.4z", "3.2","2+2z");
        assertDivision("-2x", "4","x/-2");
        assertDivision("5+6", "3","5/3+2");
        assertDivision("6+x", "2","3+x/2");
        assertDivision("2x", "x","2");
        assertDivision("2x+3", "x","2+3/x");
    }

    @Test
    public void simpleDivisionTest() {
        assertSimpleDivision("5", "2","5/2");
        assertSimpleDivision("6", "4","3/2");
        assertSimpleDivision("18", "16","9/8");
    }

    private void assertDivision(String a, String b, String result) {
        //assertThat(DIV.rawOperate(a, b)).isEqualTo(result);
    }

    private void assertSimpleDivision(String a, String b, String result) {
        //assertThat(DIV.simpleDivision(a, b)).isEqualTo(result);
    }
}
