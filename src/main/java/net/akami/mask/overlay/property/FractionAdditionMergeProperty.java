package net.akami.mask.overlay.property;

import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.expression.Expression;

public class FractionAdditionMergeProperty implements FractionMergeProperty {

    @Override
    public boolean isApplicableFor(FractionOverlay f1, FractionOverlay f2) {
        // equals is overridden in the expression class
        return f1.equals(f2);
    }

    @Override
    public Expression merge(Expression a, Expression b) {
        // The denominator remains the same after an addition implying fractions.
        // Transforming denominators so they are similar to others is not supported yet.
        return null;//v1;
    }
}
