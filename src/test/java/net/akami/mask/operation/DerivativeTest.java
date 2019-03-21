package net.akami.mask.operation;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DerivativeTest {

    @Test
    public void diffElementTest() {
        Assertions.assertThat(Differentiator.differentiateElement("x")).isEqualTo("1");
        Assertions.assertThat(Differentiator.differentiateElement("a")).isEqualTo("1");
        Assertions.assertThat(Differentiator.differentiateElement("3")).isEqualTo("0");
        Assertions.assertThat(Differentiator.differentiateElement("-1324")).isEqualTo("0");
        Assertions.assertThat(Differentiator.differentiateElement("x^2")).isEqualTo("2*x");
        Assertions.assertThat(Differentiator.differentiateElement("x^(y+1)")).isEqualTo("(y+1)*x^y");

    }
}
