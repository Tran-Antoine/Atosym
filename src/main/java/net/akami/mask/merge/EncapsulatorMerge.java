package net.akami.mask.merge;

import net.akami.mask.overlay.ExpressionOverlay;

import java.util.List;
import java.util.Set;

public class EncapsulatorMerge implements MergeBehavior<List<ExpressionOverlay>> {

    @Override
    public boolean isMergeable(List<ExpressionOverlay> a, List<ExpressionOverlay> b) {
        return false;
    }

    @Override
    public List<ExpressionOverlay> mergeElement(List<ExpressionOverlay> a, List<ExpressionOverlay> b) {
        return null;
    }

    @Override
    public Set<Class<? extends List<ExpressionOverlay>>> getHandledTypes() {
        return null;
    }
}
