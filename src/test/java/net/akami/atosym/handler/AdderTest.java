package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.SumMathObject;
import net.akami.atosym.expression.VariableExpression;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.akami.atosym.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class AdderTest {

    private final SumOperator adder = DEFAULT.getBinaryOperator(SumOperator.class);

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

    /*@Test
    public void sinusCosineSquaredProperty() {
        List<Monomial> singleInsight = Collections.singletonList(new Monomial('x', DEFAULT));
        ExpressionOverlay sin = new SineOperator(DEFAULT);
        ExpressionOverlay cos = new CosineOperator(DEFAULT);
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
    }*/

    @Test
    public void testSumChain() {
        MaskContext context = new MaskContext.Builder()
                .withBinaryOperators(SumOperator::new)
                .withFunctionOperators()
                .build();

        assertSum(new NumberExpression(3f), new NumberExpression(5f), context, "8.0");
        assertSum(new NumberExpression(4f), new VariableExpression('x'), context, "4.0+x");
        assertSum(new VariableExpression('x'), new VariableExpression('x'), context, "2.0x");
    }

    private void assertSum(MathObject a, MathObject b, MaskContext context, String result) {
        List<MathObject> elements = toList(a, b);
        SumOperator operator = context.getBinaryOperator(SumOperator.class);
        MathObject sum = new SumMathObject(operator, elements);
        assertThat(sum.operate().display()).isEqualTo(result);
    }

    private List<MathObject> toList(MathObject... objects) {
        return new ArrayList<>(Arrays.asList(objects));
    }

    private void assertSum(String a, String b, String result) {
        /*Expression aExp = ReducerFactory.reduce(a);
        Expression bExp = ReducerFactory.reduce(b);
        assertThat(adder.operate(aExp, bExp).toString()).isEqualTo(result);*/
    }
}
