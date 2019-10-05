package net.akami.atosym.function;

import org.junit.Test;

public class TangentTest {

    //private final TangentOperator function = new TangentOperator();

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
        //Assertions.assertThat(function.rawOperate(ReducerFactory.reduce(input)).toString()).isEqualTo(result);
    }
}
