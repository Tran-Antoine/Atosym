package net.akami.mask.encapsulator.property;

import net.akami.mask.encapsulator.FractionEncapsulator;
import net.akami.mask.expression.Expression;

public class FractionAdditionMergeProperty implements FractionMergeProperty {

    @Override
    public boolean isApplicableFor(FractionEncapsulator f1, FractionEncapsulator f2) {
        // equals is overridden in the expression class
        return f1.equals(f2);
    }

    @Override
    public Expression merge(Expression a, Expression b) {
        // The denominator remains the same after an addition implying fractions.
        // Transforming denominators so they are similar to others is not supported yet.
        return v1;
    }
}
