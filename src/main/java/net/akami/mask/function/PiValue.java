package net.akami.mask.function;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;

public class PiValue extends MathFunction<Expression> {

    // The size has to be 1, no param is considered as 0
    public PiValue(MaskContext context) {
        super('&', "pi", context, 1);
    }

    @Override
    protected Expression operate(Expression... input) {
        return Expression.of((float) Math.PI);
    }
}
