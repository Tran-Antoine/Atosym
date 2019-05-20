package net.akami.mask.merge;

import net.akami.mask.expression.ExpressionElement;
import net.akami.mask.expression.NumberElement;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MonomialAdditionMerge implements MergeBehavior<ExpressionElement> {

    private final HashSet handledType = new HashSet(Arrays.asList(ExpressionElement.class, NumberElement.class));

    @Override
    public boolean isMergeable(ExpressionElement a, ExpressionElement b) {
        return a.hasSameVariablePartAs(b);
    }

    @Override
    public ExpressionElement mergeElement(ExpressionElement a, ExpressionElement b) {

        // TODO : what if a and b are encapsulated
        float floatResult = new BigDecimal(a.getNumericValue()).add(new BigDecimal(b.getNumericValue())).floatValue();
        return new ExpressionElement(floatResult, a.getVariables());
    }

    @Override
    public Set<Class<? extends ExpressionElement>> getHandledTypes() {
        return handledType;
    }
}
