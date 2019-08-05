package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;

public class SquareRootFunction extends NumberRequiredFunction {

    public SquareRootFunction(MaskContext context) {
        super('£', "sqrt", context);
    }

    @Override
    protected UnaryOperation function() {
        return Math::sqrt;
    }
}
