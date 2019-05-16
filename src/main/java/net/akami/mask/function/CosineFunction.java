package net.akami.mask.function;

public class CosineFunction extends MathFunction implements TrigonometryOperation {

    public CosineFunction() {
        super('#', "cos");
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
