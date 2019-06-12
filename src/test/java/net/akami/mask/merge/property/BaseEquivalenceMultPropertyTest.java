package net.akami.mask.merge.property;

import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.SingleCharVariable;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.FractionOverlay;
import org.junit.Test;

import java.util.Optional;

import static net.akami.mask.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseEquivalenceMultPropertyTest {

    private final BaseEquivalenceMultProperty property = new BaseEquivalenceMultProperty(DEFAULT);

    @Test
    public void compatibleTest() {

        SingleCharVariable s1 = new SingleCharVariable('x', DEFAULT);
        SingleCharVariable s2 = new SingleCharVariable('x', DEFAULT);
        ComplexVariable c1 = new ComplexVariable(new Monomial('x', DEFAULT), ExponentOverlay.SQUARED);
        ComplexVariable c2 = new ComplexVariable(new Monomial('y', DEFAULT), ExponentOverlay.SQUARED);
        ComplexVariable c3 = new ComplexVariable(new Monomial('y', DEFAULT), ExponentOverlay.NULL_FACTOR);
        ComplexVariable c4 = new ComplexVariable(new Monomial('y', DEFAULT), FractionOverlay.FRACTION_NULL_FACTOR);

        assertThat(property.isApplicableFor(s1, s2)).isNotEqualTo(Optional.empty());
        assertThat(property.isApplicableFor(s1, c1)).isNotEqualTo(Optional.empty());
        assertThat(property.isApplicableFor(s1, c2)).isEqualTo(Optional.empty());
        assertThat(property.isApplicableFor(c2, c3)).isNotEqualTo(Optional.empty());
        assertThat(property.isApplicableFor(c2, c4)).isEqualTo(Optional.empty());
        assertThat(property.isApplicableFor(c3, c4)).isEqualTo(Optional.empty());
    }
}
