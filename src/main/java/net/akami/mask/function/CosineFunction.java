package net.akami.mask.function;

import net.akami.mask.core.MaskContext;

public class CosineFunction extends MathFunction implements TrigonometryOperation {

    public CosineFunction(MaskContext context) {
        super('#', "cos", context);
    }

    @Override
    protected String operate(String... input) {
        return trigonometryOperation(input[0], this.binding);
    }

    @Override
    public UnaryOperation getOperation() {
        return Math::cos;
    }

}
