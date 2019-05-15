package net.akami.mask.merge;

import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.NumberElement;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MonomialAdditionMerge implements MergeBehavior<Monomial> {

    private final HashSet handledType = new HashSet(Arrays.asList(Monomial.class, NumberElement.class));

    @Override
    public boolean isMergeable(Monomial a, Monomial b) {
        return a.hasSameVariablePartAs(b);
    }

    @Override
    public Monomial mergeElement(Monomial a, Monomial b) {
        float floatResult = new BigDecimal(a.getNumericValue()).add(new BigDecimal(b.getNumericValue())).floatValue();
        return new Monomial(floatResult, a.getVariables());
    }

    @Override
    public Set<Class<? extends Monomial>> getHandledTypes() {
        return handledType;
    }
}
