package net.akami.mask.function;

import net.akami.mask.core.MaskContext;

public class SinusFunction extends MathFunction implements TrigonometryOperation {

    public SinusFunction(MaskContext context) {
        super('@', "sin", context);
    }

    @Override
    protected String operate(String... input) {
        return trigonometryOperation(input[0], super.binding);
    }

    @Override
    public UnaryOperation getOperation() {
        return Math::sin;
    }
}
