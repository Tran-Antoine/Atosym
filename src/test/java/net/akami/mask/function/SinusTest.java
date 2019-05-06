package net.akami.mask.function;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SinusTest {

    private final SinusFunction function = new SinusFunction();

    @Test
    public void numericValuesTest() {
        assertSinus(String.valueOf(Math.toRadians(-90)), "-1.0");
        assertSinus("0.0", "0.0");
        assertSinus(String.valueOf(Math.PI), "0.0");
        assertSinus(String.valueOf(Math.PI/2), "1.0");
    }

    @Test
    public void algebraicValuesTest() {
        assertSinus("x", "(x)@");
        assertSinus("x+1", "(x+1)@");
        assertSinus("x-3^2+8.3", "(x-3^2+8.3)@");
    }


    private void assertSinus(String input, String result) {
        Assertions.assertThat(function.rawOperate(input)).isEqualTo(result);
    }
}
