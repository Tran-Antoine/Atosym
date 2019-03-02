package net.akami.mask;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.structure.EquationSolver;
import net.akami.mask.utils.ExpressionUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import net.akami.mask.structure.EquationSolver.BiMask;
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

        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e1, e2)))[0]).isEqualTo("0");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e3, e4)))[0]).isEqualTo("1");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e5, e6)))[0]).isEqualTo("-2");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e7, e8)))[0]).isEqualTo("-0.5");
    }

    @Test
    public void oneUnknownTwoDegreesTest() {
    }

    // TODO : fix problem with replacement order
    @Test
    public void twoUnknownSingleLineTest() {

        String[] r1 = solveSimple("3x", "3y");
        Assertions.assertThat(r1[0]).isEqualTo("y");
        Assertions.assertThat(r1[1]).isEqualTo("x");

        String[] r2 = solveSimple("2x", "4y");
        Assertions.assertThat(r2[0]).isEqualTo("2y");
        Assertions.assertThat(r2[1]).isEqualTo("x/2");
    }

    // TODO : use recursion, x = y/2 needs to be recalculated as soon as y is calculated
    @Test
    public void twoUnknownTwoLinesTest() {
        BiMask b1 = new BiMask(new MaskExpression("3x+2y"), new MaskExpression("7x"));
        BiMask b2 = new BiMask(new MaskExpression("6x"), new MaskExpression("3y"));

        String[] result = EquationSolver.solve(Arrays.asList(b1, b2));
        Assertions.assertThat(result[0]).isEqualTo("1");
        Assertions.assertThat(result[1]).isEqualTo("2");
    }

    public String[] solveSimple(String a, String b) {
        MaskExpression t1 = new MaskExpression(a);
        MaskExpression t2 = new MaskExpression(b);

        String[] result = EquationSolver.solve(Arrays.asList(new BiMask(t1, t2)));
        return result;
    }

    @Test
    public void getMaxPowerTest() {
        String exp = "5x^2 - 8.5x + 3x^5";
        Assertions.assertThat(ExpressionUtils.getMaximalNumericPower(exp)).isEqualTo(5);
    }
}
