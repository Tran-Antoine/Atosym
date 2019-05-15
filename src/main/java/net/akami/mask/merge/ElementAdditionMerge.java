package net.akami.mask.merge;

import net.akami.mask.expression.ExpressionElement;

import java.util.Collections;
import java.util.Set;

public class ElementAdditionMerge implements MergeBehavior<ExpressionElement> {

    @Override
    public boolean isMergeable(ExpressionElement a, ExpressionElement b) {
        if(!a.isCompatible(b)) return false;

        MergeBehavior mergeBehavior = MergeManager.getByHandledType(a.getClass());
        return mergeBehavior.isMergeable(a, b);
    }

    @Override
    public ExpressionElement mergeElement(ExpressionElement a, ExpressionElement b) {
        MergeBehavior mergeBehavior = MergeManager.getByHandledType(a.getClass());
        return (ExpressionElement) mergeBehavior.mergeElement(a, b);
    }

    @Override
    public Set<Class<? extends ExpressionElement>> getHandledTypes() {
        return Collections.singleton(ExpressionElement.class);
    }
}
