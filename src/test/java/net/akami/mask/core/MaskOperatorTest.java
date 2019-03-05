package net.akami.mask.core;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.operation.MaskOperator;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MaskOperatorTest {

    @Test
    public void imageForTest() {
        MaskOperator op = MaskOperator.begin(MaskExpression.TEMP);
        MaskExpression.TEMP.reload("5x");
        Assertions.assertThat(op.imageFor().asExpression()).isEqualTo("5x");

        Assertions.assertThat(op.replace('z', "+z", "5z")).isEqualTo("5*(+z)");
    }
}
