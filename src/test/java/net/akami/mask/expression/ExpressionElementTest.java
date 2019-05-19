package net.akami.mask.expression;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.function.CosineFunction;

import static net.akami.mask.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

import net.akami.mask.function.SinusFunction;
import net.akami.mask.function.TangentFunction;
import net.akami.mask.handler.Adder;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.VariableCombination;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExpressionElementTest {

    @Test
    public void compareDifferentLayers() {

        List<ExpressionEncapsulator> layers1 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new TangentFunction(DEFAULT)
        );
        List<ExpressionEncapsulator> layers2 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new SinusFunction(DEFAULT)
        );

        List<ExpressionElement> single = Collections.singletonList(new Monomial(1));

        ComposedVariable c1 = new ComposedVariable(single,layers1);
        ComposedVariable c2 = new ComposedVariable(single, layers2);

        assertThat(c1.equals(c2)).isEqualTo(false);
    }

    @Test
    public void compareSameLayersInDifferentOrders() {

        List<ExpressionEncapsulator> layers1 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new SinusFunction(DEFAULT)
        );
        List<ExpressionEncapsulator> layers2 = Arrays.asList(
                new SinusFunction(DEFAULT),
                new CosineFunction(DEFAULT)
        );
        List<ExpressionElement> single = Collections.singletonList(new Monomial(1));

        ComposedVariable m1 = new ComposedVariable(single, layers1);
        ComposedVariable m2 = new ComposedVariable(single, layers2);

        VariableCombination behavior = MergeManager.getByType(VariableCombination.class);
        behavior.setPropertyManager(DEFAULT.getBinaryOperation(Adder.class).getPropertyManager());
        assertThat(m1.equals(m2)).isEqualTo(false);
        assertThat(behavior.isMergeable(m1, m2)).isEqualTo(false);
    }
}
