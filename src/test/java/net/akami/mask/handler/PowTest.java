package net.akami.mask.handler;

import net.akami.mask.utils.MathUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Scanner;

public class PowTest {

    @Test
    public void powTest() {
        Assertions.assertThat(MathUtils.pow("4", "1/2")).isEqualTo("2");
    }

    @Test
    public void fastPowTest() {
        PowCalculator c = PowCalculator.getInstance();
        Assertions.assertThat(c.fastOperate("x+y", "5")).isEqualTo(MathUtils.pow("x+y", "5"));
    }
}
