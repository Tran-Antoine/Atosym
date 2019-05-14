package net.akami.mask.expression;

import static org.assertj.core.api.Assertions.assertThat;

import net.akami.mask.handler.Adder;
import net.akami.mask.handler.Multiplier;
import net.akami.mask.operation.MaskContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExpressionTester {

    private final Adder adder = new Adder(MaskContext.DEFAULT);
    private final Multiplier multiplier = new Multiplier(MaskContext.DEFAULT);

    @Test
    public void basicSum() {
        assertSimpleSum("x", "y", "x+y");
        assertSimpleSum("5.1", "0.1", "5.2");
    }

    @Test
    public void combineVars() {
        assertCombineVars(new char[]{'y'}, new char[]{'y'}, "y^2.0");
        assertCombineVars(new char[]{'x'}, new char[]{'y'}, "xy");
        assertCombineVars(new char[]{'x', 'y'}, new char[]{'x', 'y'}, "x^2.0y^2.0");
    }

    @Test
    public void simpleMult() {
        assertSimpleMult("x", "x", "x^2.0");
        assertSimpleMult("3", "2", "6.0");
        assertSimpleMult("3", "x", "3.0x");
    }

    @Test
    public void mult() {
        Expression e1 = new Expression("3").simpleMult(new Expression("y"));
        Expression e2 = new Expression("2").simpleMult(new Expression("x"));
        assertThat(e1.simpleMult(e2).toString()).isEqualTo("6.0xy");
    }

    @Test
    public void multiElementsMult() {
        Expression e1 = new Expression(new NumberElement(3), create(1, 'x'));
        Expression e2 = new Expression(new NumberElement(2), create(1, 'x'));
        System.out.println(multiplier.operate(e1, e2));
    }


    @Test
    public void multiMonomialsSum() {
        Expression e1 = new Expression(new NumberElement(2), create(3, 'x'));
        Expression e2 = new Expression(new NumberElement(3), create(2, 'x'), create(1, 'y'));

        assertThat(adder.operate(e1, e2).toString()).isEqualTo("5.0+5.0x+y");
    }

    @Test
    public void fractionTest() {
        Expression e1 = new Expression(new SimpleFraction(create(1, 'x'), create(3,'y')));
        Expression e2 = new Expression(new SimpleFraction(create(2, 'x'), create(3,'y')));

        assertThat(adder.operate(e1, e2).toString()).isEqualTo("(3.0x)/(3.0y)");
    }

    @Test
    public void multiGenericSum() {
        Expression e1 = new Expression(create(2, 'x'));
        Expression e2 = new Expression(new SimpleFraction(create(1, 'x'), create(1, 'y')));

        assertThat(adder.operate(e1, e2).toString()).isEqualTo("2.0x+x/y");
    }

    @Test
    public void bigDecimalTest() {
        Expression e1 = new Expression(new NumberElement(0.171f));
        Expression e2 = new Expression(new NumberElement(0.121f));
        assertThat(adder.simpleSum(e1, e2).toString()).isEqualTo("0.292");
    }

    private Monomial create(float a, char v) {
        return new Monomial(a, new Variable[]{new Variable(v, null, null)});
    }

    private void assertCombineVars(char[] v1, char[] v2, String result) {
        Variable[] variables = Variable.combine(get(v1), get(v2));
        List<String> converted = Arrays.asList(variables)
                .stream()
                .map(Variable::getExpression)
                .collect(Collectors.toList());

        Assertions.assertThat(String.join("", converted)).isEqualTo(result);
    }

    private Variable[] get(char... input) {
        Variable[] vars = new Variable[input.length];

        int i = 0;
        for(char s : input) {
            vars[i++] = new Variable(s, null, null);
        }
        return vars;
    }

    private void assertSimpleSum(String a, String b, String result) {
        Expression e1 = new Expression(a);
        Expression e2 = new Expression(b);
        assertThat(adder.operate(e1, e2).toString()).isEqualTo(result);
    }

    private void assertSimpleMult(String a, String b, String result) {
        Expression e1 = new Expression(a);
        Expression e2 = new Expression(b);
        assertThat(e1.simpleMult(e2).toString()).isEqualTo(result);
    }
}
