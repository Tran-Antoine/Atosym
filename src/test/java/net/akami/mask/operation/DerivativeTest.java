package net.akami.mask.operation;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DerivativeTest {

    @Test
    public void diffElementTest() {
        Assertions.assertThat(Derivative.differentiateElement("x")).isEqualTo("1");
        Assertions.assertThat(Derivative.differentiateElement("a")).isEqualTo("1");
        Assertions.assertThat(Derivative.differentiateElement("3")).isEqualTo("0");
        Assertions.assertThat(Derivative.differentiateElement("-1324")).isEqualTo("0");
        Assertions.assertThat(Derivative.differentiateElement("x^2")).isEqualTo("2*x");
        Assertions.assertThat(Derivative.differentiateElement("x^(y+1)")).isEqualTo("(y+1)*x^(y)");

    }
}
