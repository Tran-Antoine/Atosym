package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.overlay.property.CommonDenominatorAdditionProperty;

import java.util.Collections;

public class SimpleDenominatorAdditionMerge implements MergeBehavior<Monomial> {

    private MaskContext context;
    private CommonDenominatorAdditionProperty parent;

    public SimpleDenominatorAdditionMerge(MaskContext context, CommonDenominatorAdditionProperty parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    public boolean isMergeable(Monomial a, Monomial b) {
        return parent.isApplicable(a, b).isPresent();
    }

    @Override
    public MergeResult<Monomial> mergeElement(Monomial a, Monomial b) {
        Expression numerator = parent.getDividend(a, b);
        FractionOverlay divisor = FractionOverlay.fromExpression(parent.getDivisor(a));
        ComplexVariable variable = new ComplexVariable(numerator.getElements(), Collections.singletonList(divisor));
        return new MergeResult<>(new Monomial(1, variable), false);
    }

    @Override
    public Class<? extends Monomial> getHandledType() {
        return null;
    }
}
