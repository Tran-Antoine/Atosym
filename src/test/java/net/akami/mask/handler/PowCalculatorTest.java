package net.akami.mask.handler;

import net.akami.mask.utils.MathUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PowCalculatorTest {

    @Test
    public void powTest() {
        Assertions.assertThat(MathUtils.pow("4", "1/2")).isEqualTo("2");
    }
}
