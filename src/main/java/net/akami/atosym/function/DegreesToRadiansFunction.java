package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;

public class DegreesToRadiansFunction extends NumberRequiredFunction {

    public DegreesToRadiansFunction(MaskContext context) {
        super('¬', "degToRad", context);
    }
    @Override
    protected UnaryOperation function() {
        return Math::toRadians;
    }
}
