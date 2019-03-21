package net.akami.mask.operation;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MultiplicationTest {

    @Test
    public void inFormatTest() {
        Multiplicator mult = Multiplicator.getInstance();
        Assertions.assertThat(mult.inFormat("8x+y-3")).isEqualTo("8x+y-3");
    }
}
