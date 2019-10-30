package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;

public class SubOperator extends BinaryOperator {

    public SubOperator(MaskContext context) {
        super(context, "-", "sub");
    }

    @Override
    protected MathObject binaryOperate(MathObject a, MathObject b) {
        LOGGER.debug("SubOperator process of {} |-| {}: \n", a, b);
        SumOperator adder = context.getOperator(SumOperator.class);
        MultOperator multiplier = context.getOperator(MultOperator.class);

        MathObject oppositeB = multiplier.binaryOperate(MathObject.SIGN_INVERSION_MULT, b);
        return adder.binaryOperate(a, oppositeB);
    }
}
