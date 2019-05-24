package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.ReducerFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DividerTest {

    private final Divider divider = MaskContext.DEFAULT.getBinaryOperation(Divider.class);
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
        assertDivision("4", "2","2.0");
        assertDivision("6.4+6.4z", "3.2","2.0z+2.0");
        assertDivision("-2x", "4","x/-2.0");
        assertDivision("5+6", "3","3.6666667");
        assertDivision("6+x", "2","x/2.0+3.0");
        assertDivision("2x", "x","2.0");
        assertDivision("2x+3", "x","2.0+3.0/x");
    }

    @Test
    public void simpleDivisionTest() {
        assertDivision("5", "2","2.5");
        assertDivision("6", "4","1.5");
        assertDivision("18", "16","1.125");
    }

    private void assertDivision(String a, String b, String result) {
        Expression aExp = ReducerFactory.reduce(a);
        Expression bExp = ReducerFactory.reduce(b);
        assertThat(divider.operate(aExp, bExp).toString()).isEqualTo(result);
    }
}
