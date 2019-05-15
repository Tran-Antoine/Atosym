package net.akami.mask.expression;

public interface ExpressionElement {

    String getExpression();

    default boolean isCompatible(ExpressionElement other) {
        return getClass().equals(other.getClass());
    }
}
