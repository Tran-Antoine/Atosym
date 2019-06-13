package net.akami.mask.merge.property;

import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.SingleCharVariable;
import net.akami.mask.expression.Variable;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.FractionOverlay;
import org.junit.Test;

import static net.akami.mask.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseEquivalenceMultPropertyTest {


    @Test
    public void compatibleTest() {

        SingleCharVariable s1 = new SingleCharVariable('x', DEFAULT);
        SingleCharVariable s2 = new SingleCharVariable('x', DEFAULT);
        ComplexVariable c1 = new ComplexVariable(new Monomial('x', DEFAULT), ExponentOverlay.SQUARED);
        ComplexVariable c2 = new ComplexVariable(new Monomial('y', DEFAULT), ExponentOverlay.SQUARED);
        ComplexVariable c3 = new ComplexVariable(new Monomial('y', DEFAULT), ExponentOverlay.NULL_FACTOR);
        ComplexVariable c4 = new ComplexVariable(new Monomial('y', DEFAULT), FractionOverlay.FRACTION_NULL_FACTOR);

        assertThat(genProperty(s1, s2).isSuitable()).isNotEqualTo(false);
        assertThat(genProperty(s1, s2).isSuitable()).isNotEqualTo(false);
        assertThat(genProperty(s1, s2).isSuitable()).isEqualTo(false);
        assertThat(genProperty(s1, s2).isSuitable()).isNotEqualTo(false);
        assertThat(genProperty(s1, s2).isSuitable()).isEqualTo(false);
        assertThat(genProperty(s1, s2).isSuitable()).isEqualTo(false);
    }

    private ElementSequencedMergeProperty<Variable> genProperty(Variable v1, Variable v2) {
        return new BaseEquivalenceMultProperty(v1, v2, DEFAULT);
    }
}
