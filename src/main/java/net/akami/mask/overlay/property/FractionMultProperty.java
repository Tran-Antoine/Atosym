package net.akami.mask.overlay.property;

import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.expression.Expression;

public class FractionMultProperty implements FractionMergeProperty {

    @Override
    public boolean isApplicableFor(FractionOverlay f1, FractionOverlay f2) {
        return true;
    }

    @Override
    public Expression merge(Expression a, Expression b) {
        return null;
    }
}
