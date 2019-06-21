package net.akami.mask.function;

import net.akami.mask.core.MaskContext;
import net.akami.mask.function.TrigonometryOperation.UnaryOperation;

public class SquareRootFunction extends NumberRequiredFunction {

    public SquareRootFunction(MaskContext context) {
        super('£', "sqrt", context);
    }

    @Override
    protected UnaryOperation function() {
        return Math::sqrt;
    }
}
