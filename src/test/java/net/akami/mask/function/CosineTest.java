package net.akami.mask.function;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CosineTest {

    private final CosineFunction function = new CosineFunction();

    @Test
    public void numericValuesTest() {
        assertCosine(String.valueOf(Math.toRadians(-90)), "0.0");
        assertCosine("0.0", "1.0");
        assertCosine(String.valueOf(Math.PI), "-1.0");
        assertCosine(String.valueOf(Math.PI/2), "0.0");
    }

    @Test
    public void algebraicValuesTest() {
        assertCosine("x", "(x)#");
        assertCosine("x+1", "(x+1)#");
        assertCosine("x-3^2+8.3", "(x-3^2+8.3)#");
    }


    private void assertCosine(String input, String result) {
        Assertions.assertThat(function.rawOperate(input)).isEqualTo(result);
    }
}
