package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;

import java.util.List;

public class SubOperator extends BinaryOperator {

    public SubOperator(MaskContext context) {
        super(context, "-", "sub");
    }

    @Override
    protected MathObject operate(List<MathObject> input) {
        if(input.size() == 1) {
            return binaryOperate(MathObject.NEUTRAL_SUB, input.get(0));
        }
        return super.operate(input);
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {
        LOGGER.debug("SubOperator process of {} |-| {}: \n", a, b);
        SumOperator adder = context.getOperator(SumOperator.class);
        MultOperator multiplier = context.getOperator(MultOperator.class);

        MathObject oppositeB = multiplier.binaryOperate(MathObject.SIGN_INVERSION_MULT, b);
        return adder.binaryOperate(a, oppositeB);
    }

    @Override
    protected void checkInputSize(int size) {

    }
}
