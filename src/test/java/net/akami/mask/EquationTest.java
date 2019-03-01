package net.akami.mask;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.structure.EquationCalculator;
import net.akami.mask.utils.ExpressionUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import net.akami.mask.structure.EquationCalculator.BiMask;
public class EquationTest {

    @Test
    public void oneUnknownOneDegreeTest() {
        MaskExpression e1 = new MaskExpression("5x+1+2x+6");
        MaskExpression e2 = new MaskExpression("3x+3+4+x");

        MaskExpression e3 = new MaskExpression("5x+1");
        MaskExpression e4 = new MaskExpression("3x+3");

        MaskExpression e5 = new MaskExpression("4x + 9");
        MaskExpression e6 = new MaskExpression("3x + 7");

        MaskExpression e7 = new MaskExpression("5x + 5");
        MaskExpression e8 = new MaskExpression("3x + 4");

        Assertions.assertThat(EquationCalculator.solve(Arrays.asList(new BiMask(e1, e2)))).isEqualTo("0");
        Assertions.assertThat(EquationCalculator.solve(Arrays.asList(new BiMask(e3, e4)))).isEqualTo("1");
        Assertions.assertThat(EquationCalculator.solve(Arrays.asList(new BiMask(e5, e6)))).isEqualTo("-2");
        Assertions.assertThat(EquationCalculator.solve(Arrays.asList(new BiMask(e7, e8)))).isEqualTo("-0.5");
    }

    @Test
    public void oneUnknownTwoDegreesTest() {

    }

    @Test
    public void twoUnknownTest() {
        MaskExpression e5 = new MaskExpression("3x");
        MaskExpression e6 = new MaskExpression("3y");

        Assertions.assertThat(EquationCalculator.solve(Arrays.asList(new BiMask(e5, e6)))).isEqualTo("y");
    }

    @Test
    public void getMaxPowerTest() {
        String exp = "5x^2 - 8.5x + 3x^5";
        Assertions.assertThat(ExpressionUtils.getMaximalNumericPower(exp)).isEqualTo(5);
    }
}
