package net.akami.mask.operation;

import net.akami.mask.utils.MathUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PowTest {

    @Test
    public void powTest() {
        Assertions.assertThat(MathUtils.pow("4", "1/2")).isEqualTo("2");
    }
}
