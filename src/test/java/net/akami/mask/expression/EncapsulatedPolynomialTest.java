package net.akami.mask.expression;

import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.function.CosineFunction;
import net.akami.mask.function.SineFunction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static net.akami.mask.core.MaskContext.DEFAULT;

public class EncapsulatedPolynomialTest {

    @Test
    public void getExpressionTest() {

        List<Monomial> monomials = Arrays.asList(new Monomial(5, new SingleCharVariable('x', DEFAULT)),
                new NumberElement(3));

        List<ExpressionOverlay> layers = Arrays.asList(
                new ExponentOverlay(monomials),
                new CosineFunction(DEFAULT),
                new SineFunction(DEFAULT)
        );
        IntricateVariable polynomial = new IntricateVariable(monomials, layers);

        assertThat(polynomial.getExpression()).isEqualTo("sin(cos((5.0x+3.0)^(5.0x+3.0)))");
    }
}