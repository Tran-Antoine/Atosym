package net.akami.mask.utils;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static net.akami.mask.utils.ExpressionUtils.*;

public class ExpressionUtilsTest {

    @Test
    public void numericValueOfTest() {
        assertNumericValue("((x)#)^2","1");
        assertNumericValue("((x)#)((x)#)","1");
        assertNumericValue("((x)#)","1");
        assertNumericValue("5.123E10x","5.123E10");
        assertNumericValue("5x","5");
        assertNumericValue("0.3xyz","0.3");
        assertNumericValue("x^2","1");
        assertNumericValue("3x/2","1.5");
        assertNumericValue("-3x","-3");
        assertNumericValue("-1","-1");
        assertNumericValue("0.4","0.4");
        assertNumericValue("3x^(2y+1)","3");
        assertNumericValue("3((x)@)","3");
        assertNumericValue("3@","3@");
        assertNumericValue("((3x)@)","1");
        assertNumericValue("1*y^2","1");
        assertNumericValue("3x^2y^2","3");
        assertNumericValue("(3x)@", "1");
        assertThat(toNumericValue("x/3").matches("0\\.[3]+")).isEqualTo(true);
        assertThat(toNumericValue("2*y/3").matches("0\\.[6]+7")).isEqualTo(true);
    }

    private void assertNumericValue(String input, String result) {
        assertThat(ExpressionUtils.toNumericValue(input)).isEqualTo(result);
    }

    @Test
    public void toMonomialsTest() {
        System.out.println(toMonomials("(x+1)@"));
        assertThat(toMonomials("0.66y-1.66").toString()).isEqualTo("[0.66y, -1.66]");
    }

    @Test
    public void cancelMultShortcutTest() {
        assertThat(FormatterFactory.addMultiplicationSigns("3x*(4x^2-3x) + 3/4", false)).isEqualTo("3*x*(4*x^2-3*x)+3/4");
    }

    @Test
    public void toVariablesTest() {
        assertToVariables("((x)#)^2","((x)#)^2");
        assertToVariables("((x)#)((x)#)","((x)#)^2");
        assertToVariables("x^11x","x^12");
        assertToVariables("5x^2y","x^2y");
        assertToVariables("2x","x");
        assertToVariables("x*x","x^2");
        assertToVariables("xx","x^2");
        assertToVariables("x^y","x^y");
        assertToVariables("x^(y^2)","x^(y^2)");
        assertToVariables("1x^1y","xy");
        assertToVariables("x^2y^2","x^2y^2");
        assertThat(String.join("",toVariables("5((x)@)"))).isEqualTo("((x)@)");
        assertThat(String.join("",toVariables("5((4)@)"))).isEqualTo("");
    }

    private void assertToVariables(String input, String result) {
        assertThat(toVariables(input)).isEqualTo(result);
    }

    @Test
    public void clearNonVariablesTest() {
        assertClearNonVariables("5((x)@)","((x)@)^1");
        assertClearNonVariables("5((4)@)","");
    }

    private void assertClearNonVariables(String input, String result) {
        assertThat(String.join("",clearNonVariables(input))).isEqualTo(result);
    }
}

