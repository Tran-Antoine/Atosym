package net.akami.mask.expression;


public abstract class ExpressionElement {

    public abstract String getRawExpression();

    public final String getExpression() {
        return getRawExpression();
    }

    public boolean isCompatibleWith(ExpressionElement other) {
        return getClass().equals(other.getClass());
    }

}
