package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;
import net.akami.mask.handler.Adder;
import net.akami.mask.overlay.property.MergePropertyManager;

public class MonomialAdditionMerge implements MergeBehavior<Monomial> {

    private MaskContext context;

    public MonomialAdditionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isMergeable(Monomial a, Monomial b) {
        if(a.getVarPart().isSimple() && b.getVarPart().isSimple()) {
            return a.hasSameVariablePartAs(b);
        }
        MergePropertyManager propertyManager = context.getBinaryOperation(Adder.class).getPropertyManager();
        // TODO : avoid looping twice, one here and the second one in the mergeElement() method
        return propertyManager.hasOverallAppliance(a, b);
    }

    @Override
    public MergeResult<Monomial> mergeElement(Monomial a, Monomial b) {
        Adder adder = context.getBinaryOperation(Adder.class);
        if(a.getVarPart().isSimple() && b.getVarPart().isSimple()) {
            return new MergeResult<>(adder.simpleSum(a, b), false);
        }
        return adder.complexSum(a, b);
    }

    @Override
    public Class<? extends Monomial> getHandledType() {
        return Monomial.class;
    }


}
