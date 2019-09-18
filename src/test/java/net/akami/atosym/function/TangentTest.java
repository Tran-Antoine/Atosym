package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.utils.ReducerFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TangentTest {

    private final TangentOperator function = new TangentOperator(MaskContext.DEFAULT);

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
        Assertions.assertThat(function.rawOperate(ReducerFactory.reduce(input)).toString()).isEqualTo(result);
    }
}
