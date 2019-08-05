package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;

/**
 * The Sine trigonometric function, taking a single argument, computing a result between -1 and 1 if the input
 * is a number, computing nothing otherwise.
 */
public class SineFunction extends NumberRequiredFunction implements AngleUnitDependent {

    public SineFunction(MaskContext context) {
        super('@', "sin", context);
    }

    @Override
    protected UnaryOperation function() {
        return Math::sin;
    }
}
