package net.akami.mask.merge.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.overlay.FractionOverlay;

import java.util.Collections;
import java.util.List;

/**
 * The {@link CommonDenominatorAdditionProperty} performs an extra division at the end. This subclass avoids it.
 * It serves the divisor, which guarantees that the extra division is not required. By using it, infinite recursive
 * loop is prevented from happening.
 */
public class SimpleDenominatorAdditionProperty extends CommonDenominatorAdditionProperty {

    public SimpleDenominatorAdditionProperty(Monomial m1, Monomial m2, MaskContext context) {
        super(m1, m2, context);
    }

    @Override
    public void blendResult(List<Monomial> constructed) {
        Expression numerator = getDividend(p1, p2);
        FractionOverlay divisor = FractionOverlay.fromExpression(getDivisor(p1));
        ComplexVariable variable = new ComplexVariable(numerator.getElements(), Collections.singletonList(divisor));
        constructed.add(new Monomial(1, variable));
    }
}
