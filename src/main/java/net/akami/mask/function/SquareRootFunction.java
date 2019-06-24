package net.akami.mask.function;

import net.akami.mask.core.MaskContext;

public class SquareRootFunction extends NumberRequiredFunction {

    public SquareRootFunction(MaskContext context) {
        super('£', "sqrt", context);
    }

    @Override
    protected UnaryOperation function() {
        return Math::sqrt;
    }
}
