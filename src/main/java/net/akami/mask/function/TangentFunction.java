package net.akami.mask.function;

import net.akami.mask.core.MaskContext;

/**
 * The Tangent trigonometric function, taking a single argument, computing a result between -1 and 1 if the input
 * is a number, otherwise computes nothing.
 */
public class TangentFunction extends MathFunction implements TrigonometryOperation {

    public TangentFunction(MaskContext context) {
        super('§', "tan", context);
    }

    @Override
    protected String operate(String... input) {
        return trigonometricOperation(input[0], this.binding);
    }

    @Override
    public UnaryOperation getOperation() {
        return Math::tan;
    }
}
