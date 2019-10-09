package net.akami.atosym.handler;

import org.junit.Test;

public class TangentTest {

    //private final TangentOperator operator = new TangentOperator();

    @Test
    public void numericValuesTest() {
        assertTangent("0.0", "0");
        assertTangent(String.valueOf(Math.PI), "0");
    }

    @Test
    public void algebraicValuesTest() {
        assertTangent("x", "tan(x)");
        assertTangent("x+1", "tan(x+1.0)");
        assertTangent("x-3^2+8.3", "tan(x-0.6999998)");
    }

    private void assertTangent(String input, String result) {
        //Assertions.assertThat(operator.rawOperate(FastAtosymMath.reduce(input)).toString()).isEqualTo(result);
    }
}
