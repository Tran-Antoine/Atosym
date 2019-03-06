package net.akami.mask.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static net.akami.mask.utils.ExpressionUtils.*;

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
        Assertions.assertThat(toNumericValue("3(x)@")).isEqualTo("3");
        Assertions.assertThat(toNumericValue("3@")).isEqualTo("3@");
        Assertions.assertThat(toNumericValue("(3x)@")).isEqualTo("1");
        Assertions.assertThat(toNumericValue("1*y^2")).isEqualTo("1");
        Assertions.assertThat(toNumericValue("3x^2y^2")).isEqualTo("3");
    }

    @Test
    public void toMonomialsTest() {
        Assertions.assertThat(toMonomials("0.66y-1.66").toString()).isEqualTo("[0.66y, -1.66]");
    }

    @Test
    public void toVariablesTest() {
        Assertions.assertThat(toVariables("5x^2y")).isEqualTo("x^2y");
        Assertions.assertThat(toVariables("2x")).isEqualTo("x");
        Assertions.assertThat(toVariables("x*x")).isEqualTo("x^2");
        Assertions.assertThat(toVariables("xx")).isEqualTo("x^2");
        Assertions.assertThat(toVariables("x^y")).isEqualTo("x^y");
        Assertions.assertThat(toVariables("x^(y^2)")).isEqualTo("x^y^2");
        Assertions.assertThat(toVariables("1x^1y")).isEqualTo("x^y");


    }

    @Test
    public void toNumericValueTest() {
        System.out.println(ExpressionUtils.toNumericValue("(3x)@"));
    }
}

