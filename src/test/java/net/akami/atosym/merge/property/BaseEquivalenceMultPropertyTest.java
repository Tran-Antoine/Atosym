package net.akami.atosym.merge.property;

import net.akami.atosym.expression.IntricateVariable;
import net.akami.atosym.expression.Monomial;
import net.akami.atosym.expression.SingleCharVariable;
import net.akami.atosym.expression.Variable;
import net.akami.atosym.overlay.ExponentOverlay;
import net.akami.atosym.overlay.FractionOverlay;
import org.junit.Test;

import static net.akami.atosym.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseEquivalenceMultPropertyTest {


    @Test
    public void compatibleTest() {

        SingleCharVariable s1 = new SingleCharVariable('x', DEFAULT);
        SingleCharVariable s2 = new SingleCharVariable('x', DEFAULT);
        IntricateVariable c1 = new IntricateVariable(new Monomial('x', DEFAULT), ExponentOverlay.SQUARED);
        IntricateVariable c2 = new IntricateVariable(new Monomial('y', DEFAULT), ExponentOverlay.SQUARED);
        IntricateVariable c3 = new IntricateVariable(new Monomial('y', DEFAULT), ExponentOverlay.NULL_FACTOR);
        IntricateVariable c4 = new IntricateVariable(new Monomial('y', DEFAULT), FractionOverlay.FRACTION_NULL_FACTOR);

        assertThat(genProperty(s1, s2).isSuitable()).isNotEqualTo(false);
        assertThat(genProperty(c1, s2).isSuitable()).isNotEqualTo(false);
        assertThat(genProperty(c1, c2).isSuitable()).isEqualTo(false);
        assertThat(genProperty(c3, c4).isSuitable()).isEqualTo(false);
    }

    private ElementSequencedMergeProperty<Variable> genProperty(Variable v1, Variable v2) {
        return new BaseEquivalenceMultProperty(v1, v2, DEFAULT);
    }
}
