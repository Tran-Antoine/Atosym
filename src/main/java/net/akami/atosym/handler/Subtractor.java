package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.Expression;

public class Subtractor extends BinaryOperationHandler {

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
