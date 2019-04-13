package net.akami.mask.handler;

import net.akami.mask.utils.MathUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MultiplicatorTest {

    @Test
    public void multTest() {
        Assertions.assertThat(MathUtils.mult("3", "x/3")).isEqualTo("x");
    }
    @Test
    public void inFormatTest() {
        Multiplicator mult = Multiplicator.getInstance();
        Assertions.assertThat(mult.inFormat("8x+y-3")).isEqualTo("8x+y-3");
    }
}
