package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;

/**
 * The Cosine trigonometric function, taking a single argument, computing a result between -1 and 1 if the input
 * is a number, otherwise computes nothing.
 */
public class CosineFunction extends NumberRequiredFunction implements AngleUnitDependent {

    public CosineFunction(MaskContext context) {
        super('#', "cos", context);
    }

    @Override
    protected UnaryOperation function() {
        return Math::cos;
    }
}
