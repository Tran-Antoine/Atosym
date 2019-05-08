package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;

public class FunctionElement implements ExpressionElement {

    private String expression;
    private MathFunction function;

    public FunctionElement(String expression, MathFunction function) {
        this.expression = expression;
        this.function = function;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}
