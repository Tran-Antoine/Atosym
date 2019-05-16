package net.akami.mask.function;

public class SinusFunction extends MathFunction implements TrigonometryOperation {

    public SinusFunction() {
        super('@', "sin");
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
