package net.akami.mask.handler;

import net.akami.mask.utils.MathUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DividerTest {

    // It won't support factorisation for now. Therefore :
    // (x^2 + 2x + 1) / (x+1) won't give (x+1)
    @Test
    public void decomposeExpressionTest() {
        Divider div = Divider.getInstance();
        Assertions.assertThat(div.simpleDivision("4", "2")).isEqualTo("2");
        Assertions.assertThat(div.simpleDivision("5", "2")).isEqualTo("5/2");
        Assertions.assertThat(div.simpleDivision("6", "4")).isEqualTo("3/2");
        Assertions.assertThat(div.simpleDivision("18", "16")).isEqualTo("9/8");

        StringBuilder builder = new StringBuilder();
        MathUtils.decomposeNumber(18).forEach(x -> builder.append(x).append("*"));
        builder.deleteCharAt(builder.length()-1);
        Assertions.assertThat(builder.toString()).isEqualTo("2*3*3");
    }

    @Test
    public void divisionsTest() {
        Assertions.assertThat(MathUtils.divide("5+6", "3")).isEqualTo("5/3+2");
        Assertions.assertThat(MathUtils.divide("6+x", "2")).isEqualTo("3+x/2");
        Assertions.assertThat(MathUtils.divide("2x", "x")).isEqualTo("2");
        Assertions.assertThat(MathUtils.divide("2x+3", "x")).isEqualTo("2+3/x");
    }
}
