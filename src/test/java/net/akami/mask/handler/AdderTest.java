package net.akami.mask.handler;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AdderTest {

    private static final Adder SUM = new Adder();

    @Test
    public void sumTest() {
        assertSum("2", "5","7");
        assertSum("2x", "5","2x+5");
        assertSum("3x", "5x","8x");
        assertSum("x/2", "x/2","1.0x");
        assertSum("1/2", "1/2","1");
        assertSum("x+1", "-1","x");
        assertSum("3x-2", "x","4x-2");
        assertSum("((x)#)^2", "","((x)#)^2");
        assertSum("(y^2+y)", "0","y^2+y");
        assertSum("y^2", "y","y^2+y");
        assertSum("2xyz+2xyz+2xyz", "","6xyz");
        assertSum("1", "5x^2y","1+5x^2y");
        assertSum("0.4", "y","0.4+y");
        assertSum("1/2", "y",("0.5+y"));
        assertSum("2/5", "y","0.4+y");
    }

    @Test
    public void monomialSumTest() {
        List<String> monomials = Arrays.asList("2xyz", "2xyz", "2xyz");
        assertThat(SUM.monomialSum(monomials, true)).isEqualTo("6xyz");
    }

    @Test
    public void inFormatTest() {
        assertThat(SUM.inFormat("5x")).isEqualTo("5x");
        assertThat(SUM.inFormat("5x/5")).isEqualTo("1x");
        assertThat(SUM.inFormat("3x/4")).isEqualTo("0.75x");
    }

    private void assertSum(String a, String b, String result) {
        assertThat(SUM.rawOperate(a, b)).isEqualTo(result);
    }
}
