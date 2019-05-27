package net.akami.mask.structure;

import net.akami.mask.core.Mask;
import net.akami.mask.structure.EquationSolver.BiMask;
import net.akami.mask.utils.ExpressionUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
public class EquationTest {

    @Test
    public void oneUnknownOneDegreeTest() {
        Mask e1 = new Mask("5x+1+2x+6");
        Mask e2 = new Mask("3x+3+4+x");

        Mask e3 = new Mask("5x+1");
        Mask e4 = new Mask("3x+3");

        Mask e5 = new Mask("4x + 9");
        Mask e6 = new Mask("3x + 7");

        Mask e7 = new Mask("5x + 5");
        Mask e8 = new Mask("3x + 4");

        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e1, e2))).get('x')).isEqualTo("0");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e3, e4))).get('x')).isEqualTo("1.0");
        Assertions.assertThat(EquationSolver.solve(Arrays.asList(new BiMask(e5, e6))).get('x')).isEqualTo("-2.0");
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

        List<BiMask> test = EquationSolver.build("x=y", "x+y=2");
        Map<Character, String> testSolved = EquationSolver.solve(test);
        Assertions.assertThat(testSolved.get('x')).isEqualTo("1.0");
        Assertions.assertThat(testSolved.get('y')).isEqualTo("1.0");


        BiMask b5 = new BiMask(new Mask("5x+2y+7z"), new Mask("2.0"));
        BiMask b6 = new BiMask(new Mask("2x+y-3z"), new Mask("7.0"));
        BiMask b7 = new BiMask(new Mask("x+2y+z"), new Mask("4.0"));

        Map<Character, String> result2 = EquationSolver.solve(Arrays.asList(b5, b6, b7));
        Assertions.assertThat(result2.get('x')).isEqualTo("1.0");
        Assertions.assertThat(result2.get('y')).isEqualTo("2.0");
        Assertions.assertThat(result2.get('z')).isEqualTo("-1.0");

        BiMask b1 = new BiMask(new Mask("3x+2y"), new Mask("7.0x"));
        BiMask b2 = new BiMask(new Mask("6x"), new Mask("3.0y"));

        Map<Character, String> result0 = EquationSolver.solve(Arrays.asList(b1, b2));
        //Assertions.assertThat(result0[0]).isEqualTo("y/2");
        //Assertions.assertThat(result0[1]).isEqualTo("2x");

        BiMask b3 = new BiMask(new Mask("3x+5"), new Mask("2.0y"));
        BiMask b4 = new BiMask(new Mask("x+1"), new Mask("y-2.0"));

        Map<Character, String> result1 = EquationSolver.solve(Arrays.asList(b3, b4));
        Assertions.assertThat(result1.get('x')).isEqualTo("1.0");
        Assertions.assertThat(result1.get('y')).isEqualTo("4.0");

        List<BiMask> lines = EquationSolver.build("x+y+z=6", "x+2y+2z=11", "x+3y+z=10");
        Map<Character, String> result3 = EquationSolver.solve(lines);
        Assertions.assertThat(result3.get('x')).isEqualTo("1.0");
        Assertions.assertThat(result3.get('y')).isEqualTo("2.0");
        Assertions.assertThat(result3.get('z')).isEqualTo("3.0");
    }

    public Map<Character, String> solveSimple(String a, String b) {
        Mask t1 = new Mask(a);
        Mask t2 = new Mask(b);

        Map<Character, String> result = EquationSolver.solve(Arrays.asList(new BiMask(t1, t2)));
        return result;
    }

    @Test
    public void getMaxPowerTest() {
        String exp = "5x^2 - 8.5x + 3x^5";
        Assertions.assertThat(ExpressionUtils.getMaximalNumericPower(exp)).isEqualTo(5);
    }
}
