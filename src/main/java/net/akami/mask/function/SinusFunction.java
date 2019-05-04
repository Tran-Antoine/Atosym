package net.akami.mask.function;

public class SinusFunction extends MathFunction implements TrigonometryOperation {

    public SinusFunction() {
        super('@');
    }

    @Override
    protected String operate(String input) {
        return trigonometryOperation(input, super.binding);
    }

    @Override
    public UnaryOperation getOperation() {
        return Math::sin;
    }
}
