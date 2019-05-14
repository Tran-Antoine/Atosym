package net.akami.mask.expression;

public class FunctionSign implements ExpressionElement<FunctionSign> {

    private final char binding;

    public FunctionSign(char binding) {
        this.binding = binding;
    }

    @Override
    public String getExpression() {
        return String.valueOf(binding);
    }

    @Override
    public boolean isMergeable(FunctionSign other) {
        throw new RuntimeException("Illegal computation, cannot merge a function sign.");
    }

    @Override
    public FunctionSign mergeElement(FunctionSign other) {
        return null;
    }
}
