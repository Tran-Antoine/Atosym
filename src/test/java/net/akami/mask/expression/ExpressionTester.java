package net.akami.mask.expression;

import static org.assertj.core.api.Assertions.assertThat;

import net.akami.mask.handler.Adder;
import net.akami.mask.handler.Multiplier;
import net.akami.mask.operation.MaskContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;

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
    public void simpleMult() {
        assertSimpleMult("x", "x", "x^2.0");
        assertSimpleMult("3", "2", "6.0");
        assertSimpleMult("3", "x", "3.0x");
    }

    @Test
    public void mult() {
        Expression e1 = multiplier.operate(Expression.of(3), Expression.of('y'));
        Expression e2 = multiplier.operate(Expression.of(2), Expression.of('x'));
        assertThat(multiplier.operate(e1, e2).toString()).isEqualTo("6.0xy");
    }

    @Test
    public void multiElementsMult() {
        Expression e1 = new Expression(new NumberElement(3), create(1, 'x'));
        Expression e2 = new Expression(new NumberElement(2), create(1, 'x'));
        assertThat(multiplier.operate(e1, e2).toString()).isEqualTo("5.0x+6.0+x^2.0");

        Expression f1 = Expression.of(new SimpleFraction(6, Expression.of('x')));
        Expression f2 = Expression.of(3);
        Expression f3 = Expression.of(new SimpleFraction(new SimpleFraction(5, Expression.of('z')), Expression.of('y')));

        assertThat(multiplier.operate(f1, f3).toString()).isEqualTo("(30.0/z)/(xy)");
        assertThat(multiplier.operate(f1, f2).toString()).isEqualTo("18.0/x");
    }


    @Test
    public void multiMonomialsSum() {
        Expression e1 = new Expression(new NumberElement(2), create(3, 'x'));
        Expression e2 = new Expression(new NumberElement(3), create(2, 'x'), create(1, 'y'));

        assertThat(adder.operate(e1, e2).toString()).isEqualTo("5.0+5.0x+y");
    }

    @Test
    public void fractionTest() {
        Expression e1 = new Expression(new SimpleFraction(create(1, 'x'), Expression.of(create(3,'y'))));
        Expression e2 = new Expression(new SimpleFraction(create(2, 'x'), Expression.of(create(3,'y'))));

        assertThat(adder.operate(e1, e2).toString()).isEqualTo("(3.0x)/(3.0y)");
    }

    @Test
    public void multiGenericSum() {
        Expression e1 = new Expression(create(2, 'x'));
        Expression e2 = new Expression(new SimpleFraction(create(1, 'x'), Expression.of(create(1, 'y'))));

        assertThat(adder.operate(e1, e2).toString()).isEqualTo("2.0x+x/y");
    }

    @Test
    public void bigDecimalTest() {
        Expression e1 = new Expression(new NumberElement(0.171f));
        Expression e2 = new Expression(new NumberElement(0.121f));
        assertThat(adder.simpleSum(e1, e2).toString()).isEqualTo("0.292");
    }

    private Monomial create(float a, char v) {
        return new Monomial(a, new Variable(v, null, null));
    }

    private void assertSimpleSum(String a, String b, String result) {
        Expression e1 = new Expression(a);
        Expression e2 = new Expression(b);
        assertThat(adder.operate(e1, e2).toString()).isEqualTo(result);
    }

    private void assertSimpleMult(String a, String b, String result) {
        Expression e1 = new Expression(a);
        Expression e2 = new Expression(b);
        assertThat(multiplier.operate(e1, e2).toString()).isEqualTo(result);
    }
}
