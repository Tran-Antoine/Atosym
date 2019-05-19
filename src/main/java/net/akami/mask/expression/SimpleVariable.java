package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;
import net.akami.mask.core.MaskContext;

public class SimpleVariable implements Variable<SimpleVariable> {

    private final char var;
    private final Expression exponent;
    private final MathFunction function;
    private final String expression;
    private final MaskContext context;

    public SimpleVariable(char var, MaskContext context) {
        this(var, null, context);
    }

    public SimpleVariable(char var, Expression exponent, MaskContext context) {
        this(var, exponent, null, context);
    }

    public SimpleVariable(char var, Expression exponent, MathFunction function, MaskContext context) {
        this.var = var;
        this.exponent = exponent == null ? Expression.of(1) : exponent;
        this.function = function;
        this.expression = loadExpression();
        this.context = context == null ? MaskContext.DEFAULT : context;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SimpleVariable)) return false;

        SimpleVariable other = (SimpleVariable) obj;

        return var == other.var && exponent.equals(other.exponent);
    }

    private String loadExpression() {
        String result = exponent == null || exponent.toString().equals("1.0") || exponent.toString().equals("1")
                ? String.valueOf(var) : var + "^" + exponent.toString();
        return result;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    public char getVar() {
        return var;
    }

    public MathFunction getFunction() {
        return function;
    }

    public MaskContext getContext() {
        return context;
    }

    public Expression getExponent() {
        return exponent;
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public int compareTo(SimpleVariable o) {
        return this.var - o.var;
    }


}
