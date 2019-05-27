package net.akami.mask.function;

import net.akami.mask.core.MaskContext;

/**
 * The Sinus trigonometric function, taking a single argument, computing a result between -1 and 1 if the input
 * is a number, otherwise computes nothing.
 */
public class SinusFunction extends MathFunction<String> implements TrigonometryOperation {

    public SinusFunction(MaskContext context) {
        super('@', "sin", context);
    }

    @Override
    protected String operate(String... input) {
        return trigonometricOperation(input[0], super.binding);
    }

    @Override
    public UnaryOperation getOperation() {
        return Math::sin;
    }
}
