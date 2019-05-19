package net.akami.mask.expression;

import static net.akami.mask.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

import net.akami.mask.handler.Adder;
import net.akami.mask.handler.Divider;
import net.akami.mask.handler.Multiplier;

import net.akami.mask.handler.PowerCalculator;
import org.junit.Test;

public class ExpressionTester {

    private final Adder adder = new Adder(DEFAULT);
    private final Multiplier multiplier = new Multiplier(DEFAULT);
    private final Divider divider = new Divider(DEFAULT);
    private final PowerCalculator pow = new PowerCalculator(DEFAULT);

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
        assertThat(multiplier.operate(e1, e2).toString()).isEqualTo("x^2.0+5.0x+6.0");

        Expression f1 = Expression.of(new SimpleFraction(6, Expression.of('x')));
        Expression f2 = Expression.of(3);
        Expression f3 = Expression.of(new SimpleFraction(new SimpleFraction(5, Expression.of('z')), Expression.of('y')));

        assertThat(multiplier.operate(f1, f3).toString()).isEqualTo("30.0/(xyz)");
        assertThat(multiplier.operate(f1, f2).toString()).isEqualTo("18.0/x");
    }

    @Test
    public void multiMonomialsSum() {
        Expression e1 = new Expression(new NumberElement(2), create(3, 'x'));
        Expression e2 = new Expression(new NumberElement(3), create(2, 'x'), create(1, 'y'));

        assertThat(adder.operate(e1, e2).toString()).isEqualTo("5.0x+y+5.0");
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

    @Test
    public void simpleMonomialDivisionTest() {
        Monomial m1 = create(3, 'x');
        Monomial m2 = create(3, 'y');
        Monomial m3 = create(6, 'x');
        Monomial m4 = new Monomial(12, new SimpleVariable('x', Expression.of(2), DEFAULT));

        assertThat(divider.monomialDivision(m1, m2).getRawExpression()).isEqualTo("x/y");
        assertThat(divider.monomialDivision(m1, m3).getRawExpression()).isEqualTo("1.0/2.0");
        assertThat(divider.monomialDivision(m2, m3).getRawExpression()).isEqualTo("y/(2.0x)");
        assertThat(divider.monomialDivision(m4, m3).getRawExpression()).isEqualTo("2.0x");
        assertThat(divider.monomialDivision(m3, m4).getRawExpression()).isEqualTo("1.0/(2.0x)");
    }

    @Test
    public void simpleDivisionTest() {

        SimpleFraction f1 = new SimpleFraction(3, Expression.of('x'));
        SimpleFraction f2 = new SimpleFraction(4, Expression.of('x'));
        Monomial m1 = new Monomial('x', DEFAULT);
        Monomial m2 = new Monomial('y', DEFAULT);

        assertThat(divider.simpleDivision(f1, m1).get(0).getRawExpression()).isEqualTo("3.0/x^2.0");
        assertThat(divider.simpleDivision(f1, m2).get(0).getRawExpression()).isEqualTo("3.0/(xy)");
        assertThat(divider.simpleDivision(f1, f2).get(0).getRawExpression()).isEqualTo("3.0/4.0");
    }

    @Test
    public void powTest() {

        Expression e1 = Expression.of(new Monomial(3, new SimpleVariable('x', DEFAULT)));
        Expression e2 = Expression.of(3);
        Expression e3 = Expression.of('y');
        Expression e4 = Expression.of(2.5f);

        assertThat(pow.operate(e1, e2).toString()).isEqualTo("27.0x^3.0");
        assertThat(pow.operate(e1, e3).toString()).isEqualTo("(3.0x)^y");
    }

    private Monomial create(float a, char v) {
        return new Monomial(a, new SimpleVariable(v, null, null));
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
