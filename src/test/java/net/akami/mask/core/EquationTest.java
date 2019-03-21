package net.akami.mask.core;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.structure.EquationSolver;
import net.akami.mask.utils.ExpressionUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e1, e2))).get('x')).isEqualTo("0");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e3, e4))).get('x')).isEqualTo("1");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e5, e6))).get('x')).isEqualTo("-2");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e7, e8))).get('x')).isEqualTo("-0.5");
    }

    @Test
    public void oneUnknownTwoDegreesTest() {
    }

    /*
    Unused test for now
    @Test
    public void twoUnknownSingleLineTest() {

        String[] r1 = solveSimple("3x", "3y");
        Assertions.assertThat(r1[0]).isEqualTo("y");
        Assertions.assertThat(r1[1]).isEqualTo("x");

        String[] r2 = solveSimple("2x", "4y");
        Assertions.assertThat(r2[0]).isEqualTo("2y");
        Assertions.assertThat(r2[1]).isEqualTo("x/2");
    }*/

    @Test
    public void multiUnknownMultiLines() {
        BiMask b5 = new BiMask(new MaskExpression("5x+2y+7z"), new MaskExpression("2"));
        BiMask b6 = new BiMask(new MaskExpression("2x+y-3z"), new MaskExpression("7"));
        BiMask b7 = new BiMask(new MaskExpression("x+2y+z"), new MaskExpression("4"));

        Map<Character, String> result2 = EquationSolver.solve(Arrays.asList(b5, b6, b7));
        Assertions.assertThat(result2.get('x')).isEqualTo("1");
        Assertions.assertThat(result2.get('y')).isEqualTo("2");
        Assertions.assertThat(result2.get('z')).isEqualTo("-1");

        List<BiMask> test = EquationSolver.build("x=y", "x+y=2");
        Map<Character, String> testSolved = EquationSolver.solve(test);
        Assertions.assertThat(testSolved.get('x')).isEqualTo("1");
        Assertions.assertThat(testSolved.get('y')).isEqualTo("1");

        BiMask b1 = new BiMask(new MaskExpression("3x+2y"), new MaskExpression("7x"));
        BiMask b2 = new BiMask(new MaskExpression("6x"), new MaskExpression("3y"));

        Map<Character, String> result0 = EquationSolver.solve(Arrays.asList(b1, b2));
        //Assertions.assertThat(result0[0]).isEqualTo("y/2");
        //Assertions.assertThat(result0[1]).isEqualTo("2x");

        BiMask b3 = new BiMask(new MaskExpression("3x+5"), new MaskExpression("2y"));
        BiMask b4 = new BiMask(new MaskExpression("x+1"), new MaskExpression("y-2"));

        Map<Character, String> result1 = EquationSolver.solve(Arrays.asList(b3, b4));
        Assertions.assertThat(result1.get('x')).isEqualTo("1");
        Assertions.assertThat(result1.get('y')).isEqualTo("4");

        List<BiMask> lines = EquationSolver.build("x+y+z=6", "x+2y+2z=11", "x+3y+z=10");
        Map<Character, String> result3 = EquationSolver.solve(lines);
        Assertions.assertThat(result3.get('x')).isEqualTo("1");
        Assertions.assertThat(result3.get('y')).isEqualTo("2");
        Assertions.assertThat(result3.get('z')).isEqualTo("3");
    }

    public Map<Character, String> solveSimple(String a, String b) {
        MaskExpression t1 = new MaskExpression(a);
        MaskExpression t2 = new MaskExpression(b);

        Map<Character, String> result = EquationSolver.solve(Arrays.asList(new BiMask(t1, t2)));
        return result;
    }

    @Test
    public void getMaxPowerTest() {
        String exp = "5x^2 - 8.5x + 3x^5";
        Assertions.assertThat(ExpressionUtils.getMaximalNumericPower(exp)).isEqualTo(5);
    }
}
