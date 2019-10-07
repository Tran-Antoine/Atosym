package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;

public class SubOperator extends BinaryOperator {

    private MaskContext context;

    public SubOperator(MaskContext context) {
        super("sub");
        this.context = context;
    }

    @Override
    protected MathObject binaryOperate(MathObject a, MathObject b) {
        /*LOGGER.debug("SubOperator process of {} |-| {}: \n", a, b);
        SumOperator adder = context.getBinaryOperation(SumOperator.class);
        MultOperator multiplier = context.getBinaryOperation(MultOperator.class);

        Expression oppositeB = multiplier.operate(Expression.of(-1), b);
        return adder.operate(a, oppositeB);*/
        return null;
    }
}
