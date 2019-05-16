package net.akami.mask.expression;

public class FunctionSign extends ExpressionElement {

    private final char binding;

    public FunctionSign(char binding) {
        this.binding = binding;
    }

    @Override
    public String getRawExpression() {
        return String.valueOf(binding);
    }
}
