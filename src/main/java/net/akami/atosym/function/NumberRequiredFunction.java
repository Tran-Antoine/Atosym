package net.akami.atosym.function;

import net.akami.atosym.overlay.ExpressionOverlay;
import net.akami.atosym.utils.ExpressionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public abstract class NumberRequiredFunction extends MathOperator {

    public NumberRequiredFunction(String name, int argsLength) {
        super(name, argsLength);
    }

    @Override
    protected Expression operate(Expression... input) {
        if(areAllNumbers(input)) return encapsulate(input);

        return computeNumbers(input);
    }

    private Expression encapsulate(Expression... unique) {
        List<Monomial> elements = unique.getElements();
        List<ExpressionOverlay> overlays = Collections.singletonList(this);

        IntricateVariable var = new IntricateVariable(elements, overlays);
        return Expression.of(new Monomial(1, var));
    }

    private Expression computeNumbers(Expression unique) {
        double result = function().compute(Double.parseDouble(unique.toString()));
        BigDecimal decimal = new BigDecimal(result).setScale(6, BigDecimal.ROUND_HALF_UP);
        return Expression.of(decimal.floatValue());
    }

    private boolean areAllNumbers(Expression... input) {
        for(Expression exp : input) {
            if(!ExpressionUtils.isANumber(exp)) return false;
        }
        return true;
    }

    protected abstract DoubleOperation function();
}
