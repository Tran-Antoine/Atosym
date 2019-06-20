package net.akami.mask.handler;

import net.akami.mask.expression.*;
import net.akami.mask.function.CosineFunction;
import net.akami.mask.function.SinusFunction;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.utils.ReducerFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.akami.mask.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class AdderTest {

    private final Adder adder = DEFAULT.getBinaryOperation(Adder.class);

    @Test
    public void numericTest() {
        assertSum("2", "5","7.0");
    }

    @Test
    public void sumTest() {
        assertSum("2x", "5","2.0x+5.0");
        assertSum("3x", "5x","8.0x");
        assertSum("x/2", "x/2","x");
        assertSum("1/2", "1/2","1.0");
        assertSum("x+1", "-1","x");
        assertSum("3x-2", "x","4.0x-2.0");
        //assertSum("((x)#)^2", "","((x)#)^2.0");
        assertSum("(y^2+y)", "0","y^2.0+y");
        assertSum("y^2", "y","y^2.0+y");
        //assertSum("2xyz+2xyz+2xyz", "","6.0xyz");
        assertSum("1", "5x^2y","5.0x^2.0y+1.0");
        assertSum("0.4", "y","y+0.4");
        assertSum("1/2", "y",("y+0.5"));
        assertSum("2/5", "y","y+0.4");
    }

    @Test
    public void sinusCosineSquaredProperty() {
        List<Monomial> singleInsight = Collections.singletonList(new Monomial('x', DEFAULT));
        ExpressionOverlay sin = new SinusFunction(DEFAULT);
        ExpressionOverlay cos = new CosineFunction(DEFAULT);
        ExpressionOverlay exponent = ExponentOverlay.fromExpression(Expression.of(2));

        SingleCharVariable simple = new SingleCharVariable('x', DEFAULT);
        List<Variable> vars1 = Arrays.asList(new IntricateVariable(singleInsight, Arrays.asList(cos, exponent)), simple);
        List<Variable> vars2 = Arrays.asList(new IntricateVariable(singleInsight, Arrays.asList(sin, exponent)), simple);

        Monomial m1 = new Monomial(4,vars1);
        Monomial m2 = new Monomial(4, vars2);
        Monomial m3 = new Monomial(3, vars1);

        assertThat(adder.operate(Expression.of(m1), Expression.of(m2)).toString()).isEqualTo("4.0x");
        assertThat(adder.operate(Expression.of(m1), Expression.of(m3)).toString()).isEqualTo("7.0cos(x)^2.0x");
    }

    @Test
    public void commonDenominatorTest() {
        ExpressionOverlay fraction = FractionOverlay.fromExpression(Expression.of(2));
        Monomial insight1 = new Monomial(1, new SingleCharVariable('x', DEFAULT));
        Monomial insight2 = new Monomial(1, new SingleCharVariable('x', DEFAULT));
        IntricateVariable complex1 = new IntricateVariable(insight1, fraction);
        IntricateVariable complex2 = new IntricateVariable(insight2, fraction);

        Monomial m1 = new Monomial(7, complex1);
        Monomial m2 = new Monomial(3, complex2);

        assertThat(adder.operate(Expression.of(m1), Expression.of(m2)).toString()).isEqualTo("5.0x");
    }

    @Test
    public void monomialSumTest() {
        List<String> monomials = Arrays.asList("2xyz", "2xyz", "2xyz");
        //assertThat(adder.monomialSum(monomials, true)).isEqualTo("6xyz");
    }


    private void assertSum(String a, String b, String result) {
        Expression aExp = ReducerFactory.reduce(a);
        Expression bExp = ReducerFactory.reduce(b);
        assertThat(adder.operate(aExp, bExp).toString()).isEqualTo(result);
    }
}
