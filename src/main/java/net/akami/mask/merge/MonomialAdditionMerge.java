package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;
import net.akami.mask.handler.Adder;
import net.akami.mask.overlay.property.MergePropertyManager;

import java.util.List;
import java.util.Optional;

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
        return propertyManager.hasOverallAppliance(a, b);
    }

    @Override
    public MergeResult<Monomial> mergeElement(Monomial a, Monomial b) {
        if(a.getVarPart().isSimple() && b.getVarPart().isSimple()) {
            return new MergeResult<>(context.getBinaryOperation(Adder.class).simpleSum(a, b), false);
        }
        MergePropertyManager propertyManager = context.getBinaryOperation(Adder.class).getPropertyManager();

        OverlayAdditionMerge additionMerge = new OverlayAdditionMerge(a, b, propertyManager.getProperties());
        Optional<List<Monomial>> result = additionMerge.merge();
        if(!result.isPresent()) throw new RuntimeException("isMergeable returned true but couldn't find a result");

        return new MergeResult<>(result.get(), additionMerge.startingOverRequested());
    }

    @Override
    public Class<? extends Monomial> getHandledType() {
        return Monomial.class;
    }


}
