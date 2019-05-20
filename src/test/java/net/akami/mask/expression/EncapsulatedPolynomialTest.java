package net.akami.mask.expression;

import net.akami.mask.encapsulator.ExponentEncapsulator;
import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.function.CosineFunction;
import net.akami.mask.function.SinusFunction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static net.akami.mask.core.MaskContext.DEFAULT;

public class EncapsulatedPolynomialTest {

    @Test
    public void getExpressionTest() {

        List<ExpressionElement> monomials = Arrays.asList(new ExpressionElement(5, new SimpleVariable('x', DEFAULT)),
                new NumberElement(3));

        List<ExpressionEncapsulator> layers = Arrays.asList(
                new ExponentEncapsulator(monomials),
                new CosineFunction(DEFAULT),
                new SinusFunction(DEFAULT)
        );
        IrreducibleVarPart polynomial = new IrreducibleVarPart(monomials, layers);

        assertThat(polynomial.getExpression()).isEqualTo("sin(cos((5.0x+3.0)^(5.0x+3.0)))");
    }
}