package net.akami.mask.expression;


public abstract class ExpressionElement<T> implements Comparable<T> {

    public abstract String getRawExpression();

    public final String getExpression() {
        return getRawExpression();
    }

    public boolean isCompatibleWith(ExpressionElement other) {
        return getClass().isAssignableFrom(other.getClass())
                || other.getClass().isAssignableFrom(getClass());
    }

    @Override
    public int compareTo(T o) {
        return 0;
    }
}
