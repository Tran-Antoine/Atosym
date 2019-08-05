package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;

/**
 * The Tangent trigonometric function, taking a single argument, computing a result if the input
 * is a number, otherwise computes nothing.
 */
public class TangentFunction extends NumberRequiredFunction implements AngleUnitDependent {

    public TangentFunction(MaskContext context) {
        super('§', "tan", context);
    }

    @Override
    protected UnaryOperation function() {
        return Math::tan;
    }
}
