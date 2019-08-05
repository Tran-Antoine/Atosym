package net.akami.atosym.expression;

import net.akami.atosym.function.CosineFunction;
import net.akami.atosym.function.SineFunction;
import net.akami.atosym.function.TangentFunction;
import net.akami.atosym.merge.SequencedMerge;
import net.akami.atosym.merge.VariableCombination;
import net.akami.atosym.overlay.ExpressionOverlay;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.akami.atosym.core.MaskContext.DEFAULT;
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
                new SineFunction(DEFAULT)
        );

        List<Monomial> single = Collections.singletonList(new NumberElement(1));

        IntricateVariable c1 = new IntricateVariable(single,layers1);
        IntricateVariable c2 = new IntricateVariable(single, layers2);

        assertThat(c1.equals(c2)).isEqualTo(false);
    }

    @Test
    public void compareSameLayersInDifferentOrders() {

        List<ExpressionOverlay> layers1 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new SineFunction(DEFAULT)
        );
        List<ExpressionOverlay> layers2 = Arrays.asList(
                new SineFunction(DEFAULT),
                new CosineFunction(DEFAULT)
        );
        List<Monomial> single = Collections.singletonList(new NumberElement(1));

        IntricateVariable m1 = new IntricateVariable(single, layers1);
        IntricateVariable m2 = new IntricateVariable(single, layers2);

        SequencedMerge<Variable> behavior = new VariableCombination(DEFAULT);
        assertThat(m1.equals(m2)).isEqualTo(false);
        assertThat(behavior.generateElementProperties(m1, m2).get(0).isSuitable()).isEqualTo(false);
    }
}
