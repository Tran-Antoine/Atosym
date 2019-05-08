package net.akami.mask.expression;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ExpressionTester {

    @Test
    public void basicSum() {
        assertSimpleSum("x", "y", "1.0x+1.0y");
        assertSimpleSum("5.1", "0.1", "5.2");
    }

    @Test
    public void combineVars() {

    }

    private void assertSimpleSum(String a, String b, String result) {
        Expression e1 = new Expression(a);
        Expression e2 = new Expression(b);
        Assertions.assertThat(e1.simpleSum(e2).toString()).isEqualTo(result);
    }
}
