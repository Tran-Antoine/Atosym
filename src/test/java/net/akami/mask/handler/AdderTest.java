package net.akami.mask.handler;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AdderTest {

    private static final Adder SUM = new Adder();
    @Test
    public void sumTest() {
        Assertions.assertThat(SUM.rawOperate("2", "5")).isEqualTo("7");
        Assertions.assertThat(SUM.rawOperate("2x", "5")).isEqualTo("2x+5");
        Assertions.assertThat(SUM.rawOperate("3x", "5x")).isEqualTo("8x");
        Assertions.assertThat(SUM.rawOperate("x/2", "x/2")).isEqualTo("1.0x");
        Assertions.assertThat(SUM.rawOperate("1/2", "1/2")).isEqualTo("1");
        Assertions.assertThat(SUM.rawOperate("x+1", "-1")).isEqualTo("x");
        Assertions.assertThat(SUM.rawOperate("3x-2", "x")).isEqualTo("4x-2");
        Assertions.assertThat(SUM.rawOperate("((x)#)^2", "")).isEqualTo("((x)#)^2");
        Assertions.assertThat(SUM.rawOperate("(y^2+y)", "0")).isEqualTo("y^2+y");
        Assertions.assertThat(SUM.rawOperate("y^2", "y")).isEqualTo("y^2+y");
        Assertions.assertThat(SUM.rawOperate("2xyz+2xyz+2xyz", "")).isEqualTo("6xyz");
        Assertions.assertThat(SUM.rawOperate("1", "5x^2y")).isEqualTo("1+5x^2y");
        Assertions.assertThat(SUM.rawOperate("0.4", "y")).isEqualTo("0.4+y");
        Assertions.assertThat(SUM.rawOperate("1/2", "y")).isEqualTo("0.5+y");
        Assertions.assertThat(SUM.rawOperate("2/5", "y")).isEqualTo("0.4+y");
    }

    @Test
    public void monomialSumTest() {
        List<String> monomials = Arrays.asList("2xyz", "2xyz", "2xyz");
        Assertions.assertThat(SUM.monomialSum(monomials, true)).isEqualTo("6xyz");
    }

    @Test
    public void inFormatTest() {
        Assertions.assertThat(SUM.inFormat("5x")).isEqualTo("5x");
        Assertions.assertThat(SUM.inFormat("5x/5")).isEqualTo("1x");
        Assertions.assertThat(SUM.inFormat("3x/4")).isEqualTo("0.75x");
    }
}
