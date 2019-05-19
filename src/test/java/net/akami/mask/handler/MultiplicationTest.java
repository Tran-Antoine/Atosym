package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import org.junit.Test;

public class MultiplicationTest {

    @Test
    public void inFormatTest() {
        Multiplier mult = MaskContext.DEFAULT.getBinaryOperation(Multiplier.class);
        //Assertions.assertThat(mult.inFormat("8x+y-3")).isEqualTo("8x+y-3");
    }
}
