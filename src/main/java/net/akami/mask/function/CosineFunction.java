package net.akami.mask.function;

import net.akami.mask.core.MaskContext;
import net.akami.mask.function.TrigonometryOperation.UnaryOperation;

/**
 * The Cosine trigonometric function, taking a single argument, computing a result between -1 and 1 if the input
 * is a number, otherwise computes nothing.
 */
public class CosineFunction extends NumberRequiredFunction {

    public CosineFunction(MaskContext context) {
        super('#', "cos", context);
    }

    @Override
    protected UnaryOperation function() {
        return Math::cos;
    }
}
