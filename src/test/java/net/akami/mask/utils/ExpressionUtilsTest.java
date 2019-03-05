package net.akami.mask.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import static net.akami.mask.utils.ExpressionUtils.toNumericValue;
import static net.akami.mask.utils.ExpressionUtils.toMonomials;
import static net.akami.mask.utils.ExpressionUtils.toVariables;
public class ExpressionUtilsTest {

    @Test
    public void numericValueOfTest() {
        Assertions.assertThat(toNumericValue("5x")).isEqualTo("5");
        Assertions.assertThat(toNumericValue("0.3xyz")).isEqualTo("0.3");
        Assertions.assertThat(toNumericValue("x^2")).isEqualTo("1");
        Assertions.assertThat(toNumericValue("3x/2")).isEqualTo("3/2");
        Assertions.assertThat(toNumericValue("x/3")).isEqualTo("1/3");
        Assertions.assertThat(toNumericValue("-3x")).isEqualTo("-3");
        Assertions.assertThat(toNumericValue("-1")).isEqualTo("-1");
        Assertions.assertThat(toNumericValue("2*y/3")).isEqualTo("2/3");
        Assertions.assertThat(toNumericValue("0.4")).isEqualTo("0.4");
        Assertions.assertThat(toNumericValue("3x^(2y+1)")).isEqualTo("3");

    }
    @Test
    public void toMonomialsTest() {
        Assertions.assertThat(toMonomials("0.66y-1.66").toString()).isEqualTo("[0.66y, -1.66]");
    }

    @Test
    public void toVariablesTest() {
        Assertions.assertThat(toVariables("5x^2y")).isEqualTo("x^2y");
    }
}
