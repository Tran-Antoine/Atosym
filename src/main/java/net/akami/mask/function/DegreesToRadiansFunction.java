package net.akami.mask.function;

import net.akami.mask.core.MaskContext;

public class DegreesToRadiansFunction extends NumberRequiredFunction {

    public DegreesToRadiansFunction(MaskContext context) {
        super('¬', "degToRad", context);
    }
    @Override
    protected UnaryOperation function() {
        return Math::toRadians;
    }
}
