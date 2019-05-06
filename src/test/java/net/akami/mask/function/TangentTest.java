package net.akami.mask.function;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TangentTest {

    private final TangentFunction function = new TangentFunction();

    @Test
    public void numericValuesTest() {
        assertTangent("0.0", "0.0");
        assertTangent(String.valueOf(Math.PI), "0.0");
    }

    @Test
    public void algebraicValuesTest() {
        assertTangent("x", "(x)§");
        assertTangent("x+1", "(x+1)§");
        assertTangent("x-3^2+8.3", "(x-3^2+8.3)§");
    }

    private void assertTangent(String input, String result) {
        Assertions.assertThat(function.rawOperate(input)).isEqualTo(result);
    }
}
