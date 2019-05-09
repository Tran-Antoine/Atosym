package net.akami.mask.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExpressionTester {

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
        assertThat(e1.simpleSum(e2).toString()).isEqualTo(result);
    }

    private void assertSimpleMult(String a, String b, String result) {
        Expression e1 = new Expression(a);
        Expression e2 = new Expression(b);
        assertThat(e1.simpleMult(e2).toString()).isEqualTo(result);
    }
}
