package net.akami.mask.handler;

import net.akami.mask.operation.MaskContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MultiplicationTest {

    @Test
    public void inFormatTest() {
        Multiplicator mult = MaskContext.DEFAULT.getBinaryOperation(Multiplicator.class);
        Assertions.assertThat(mult.inFormat("8x+y-3")).isEqualTo("8x+y-3");
    }
}
