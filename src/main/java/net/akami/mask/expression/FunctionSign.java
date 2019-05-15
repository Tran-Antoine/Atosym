package net.akami.mask.expression;

public class FunctionSign implements ExpressionElement {

    private final char binding;

    public FunctionSign(char binding) {
        this.binding = binding;
    }

    @Override
    public String getExpression() {
        return String.valueOf(binding);
    }
}
