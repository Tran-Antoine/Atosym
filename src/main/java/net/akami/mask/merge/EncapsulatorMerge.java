package net.akami.mask.merge;

import net.akami.mask.encapsulator.ExpressionEncapsulator;

import java.util.List;
import java.util.Set;

public class EncapsulatorMerge implements MergeBehavior<List<ExpressionEncapsulator>> {

    @Override
    public boolean isMergeable(List<ExpressionEncapsulator> a, List<ExpressionEncapsulator> b) {
        return false;
    }

    @Override
    public List<ExpressionEncapsulator> mergeElement(List<ExpressionEncapsulator> a, List<ExpressionEncapsulator> b) {
        return null;
    }

    @Override
    public Set<Class<? extends List<ExpressionEncapsulator>>> getHandledTypes() {
        return null;
    }
}
