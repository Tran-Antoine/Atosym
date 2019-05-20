package net.akami.mask.encapsulator.property;

import net.akami.mask.encapsulator.FractionEncapsulator;
import net.akami.mask.expression.Expression;

public class FractionMultProperty implements FractionMergeProperty {

    @Override
    public boolean isApplicableFor(FractionEncapsulator f1, FractionEncapsulator f2) {
        return true;
    }

    @Override
    public Expression merge(Expression a, Expression b) {
        return null;
    }
}
