package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;

import java.math.BigDecimal;

public class MonomialAdditionMerge implements MergeBehavior<Monomial> {

    private MaskContext context;

    public MonomialAdditionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isMergeable(Monomial a, Monomial b) {
        return a.hasSameVariablePartAs(b);
    }

    @Override
    public Monomial mergeElement(Monomial a, Monomial b) {
        // TODO : what if a and b are encapsulated
        float floatResult = new BigDecimal(a.getNumericValue()).add(new BigDecimal(b.getNumericValue())).floatValue();
        return new Monomial(floatResult, a.getVarPart());
    }

    @Override
    public Class<? extends Monomial> getHandledType() {
        return Monomial.class;
    }
}
