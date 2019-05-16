package net.akami.mask.merge;

import net.akami.mask.expression.ExpressionElement;
import net.akami.mask.expression.SimpleFraction;

import java.util.Collections;
import java.util.Set;

public class FractionAdditionMerge implements MergeBehavior<SimpleFraction> {

    @Override
    public boolean isMergeable(SimpleFraction a, SimpleFraction b) {
        if(!b.getNumerator().isCompatibleWith(a.getNumerator()))
            return false;

        MergeBehavior mergeBehavior = MergeManager.getByHandledType(a.getNumerator().getClass());
        return a.hasSameDenominatorAs(b) && mergeBehavior.isMergeable(a.getNumerator(), b.getNumerator());
    }

    @Override
    public SimpleFraction mergeElement(SimpleFraction a, SimpleFraction b) {
        MergeBehavior<ExpressionElement> mergeBehavior = MergeManager.getByType(ElementAdditionMerge.class);
        ExpressionElement newNumerator = mergeBehavior.mergeElement(a.getNumerator(), b.getNumerator());
        return new SimpleFraction(newNumerator, a.getDenominator());
    }

    @Override
    public Set<Class<? extends SimpleFraction>> getHandledTypes() {
        return Collections.singleton(SimpleFraction.class);
    }
}
