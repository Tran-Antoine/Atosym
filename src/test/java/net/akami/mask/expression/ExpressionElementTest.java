package net.akami.mask.expression;

import net.akami.mask.function.CosineFunction;
import net.akami.mask.function.SinusFunction;
import net.akami.mask.function.TangentFunction;
import net.akami.mask.merge.SequencedMerge;
import net.akami.mask.merge.VariableCombination;
import net.akami.mask.overlay.ExpressionOverlay;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.akami.mask.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionElementTest {

    @Test
    public void compareDifferentLayers() {

        List<ExpressionOverlay> layers1 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new TangentFunction(DEFAULT)
        );
        List<ExpressionOverlay> layers2 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new SinusFunction(DEFAULT)
        );

        List<Monomial> single = Collections.singletonList(new NumberElement(1));

        ComplexVariable c1 = new ComplexVariable(single,layers1);
        ComplexVariable c2 = new ComplexVariable(single, layers2);

        assertThat(c1.equals(c2)).isEqualTo(false);
    }

    @Test
    public void compareSameLayersInDifferentOrders() {

        List<ExpressionOverlay> layers1 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new SinusFunction(DEFAULT)
        );
        List<ExpressionOverlay> layers2 = Arrays.asList(
                new SinusFunction(DEFAULT),
                new CosineFunction(DEFAULT)
        );
        List<Monomial> single = Collections.singletonList(new NumberElement(1));

        ComplexVariable m1 = new ComplexVariable(single, layers1);
        ComplexVariable m2 = new ComplexVariable(single, layers2);

        SequencedMerge<Variable> behavior = new VariableCombination(DEFAULT);
        assertThat(m1.equals(m2)).isEqualTo(false);
        assertThat(behavior.generateElementProperties(m1, m2).get(0).isSuitable()).isEqualTo(false);
    }
}
