package net.akami.mask.expression;

import net.akami.mask.function.CosineFunction;
import static org.assertj.core.api.Assertions.assertThat;

import net.akami.mask.function.SinusFunction;
import net.akami.mask.function.TangentFunction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ExpressionElementTest {

    @Test
    public void compareDifferentLayers() {

        List<ExpressionEncapsulator> layers1 = Arrays.asList(
                new CosineFunction(),
                new TangentFunction()
        );
        List<ExpressionEncapsulator> layers2 = Arrays.asList(
                new CosineFunction(),
                new SinusFunction()
        );

        Monomial m1 = new NumberElement(0, layers1);
        Monomial m2 = new NumberElement(1, layers2);

        assertThat(m1.hasSameEncapsulationAs(m2)).isEqualTo(false);
    }

    @Test
    public void compareSameLayersInDifferentOrders() {

        List<ExpressionEncapsulator> layers1 = Arrays.asList(
                new CosineFunction(),
                new SinusFunction()
        );
        List<ExpressionEncapsulator> layers2 = Arrays.asList(
                new SinusFunction(),
                new CosineFunction()
        );
        Monomial m1 = new NumberElement(0, layers1);
        Monomial m2 = new NumberElement(0, layers2);

        assertThat(m1.hasSameEncapsulationAs(m2)).isEqualTo(false);
    }
}
