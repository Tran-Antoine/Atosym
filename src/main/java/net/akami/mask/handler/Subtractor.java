package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;

public class Subtractor extends BinaryOperationHandler<Expression> {

    public Subtractor(MaskContext context) {
        super(context);
    }

    @Override
    protected Expression operate(Expression a, Expression b) {
        LOGGER.debug("Subtractor process of {} |-| {}: \n", a, b);
        Adder adder = context.getBinaryOperation(Adder.class);
        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        Expression oppositeB = multiplier.operate(Expression.of(-1), b);
        return adder.operate(a, oppositeB);
    }
}
