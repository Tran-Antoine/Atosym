package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;

public class SubOperator extends BinaryOperator {

    private MaskContext context;

    public SubOperator(MaskContext context) {
        super("sub");
        this.context = context;
    }

    @Override
    protected Expression binaryOperate(Expression a, Expression b) {
        LOGGER.debug("SubOperator process of {} |-| {}: \n", a, b);
        SumOperator adder = context.getBinaryOperation(SumOperator.class);
        MultOperator multiplier = context.getBinaryOperation(MultOperator.class);

        Expression oppositeB = multiplier.operate(Expression.of(-1), b);
        return adder.operate(a, oppositeB);
    }
}
