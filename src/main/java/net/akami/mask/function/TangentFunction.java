package net.akami.mask.function;

public class TangentFunction extends MathFunction implements TrigonometryOperation {

    public TangentFunction() {
        super('§', "tan");
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
