package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.utils.ReducerFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CosineTest {

    private final CosineOperator function = new CosineOperator(MaskContext.DEFAULT);

    @Test
    public void numericValuesTest() {
        assertCosine(String.valueOf(Math.toRadians(-90)), "0");
        assertCosine("0.0", "1.0");
        assertCosine(String.valueOf(Math.PI), "-1.0");
        assertCosine(String.valueOf(Math.PI/2), "0");
    }

    @Test
    public void algebraicValuesTest() {
        assertCosine("x", "cos(x)");
        assertCosine("x+1", "cos(x+1.0)");
        assertCosine("x-3^2+8.3", "cos(x-0.6999998)");
    }


    private void assertCosine(String input, String result) {
        Assertions.assertThat(function.rawOperate(ReducerFactory.reduce(input)).toString()).isEqualTo(result);
    }
}
