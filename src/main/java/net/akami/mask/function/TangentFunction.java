package net.akami.mask.function;

import net.akami.mask.core.MaskContext;

public class TangentFunction extends MathFunction implements TrigonometryOperation {

    public TangentFunction(MaskContext context) {
        super('§', "tan", context);
    }

    @Override
    protected String operate(String... input) {
        return trigonometryOperation(input[0], this.binding);
    }

    @Override
    public UnaryOperation getOperation() {
        return Math::tan;
    }
}
