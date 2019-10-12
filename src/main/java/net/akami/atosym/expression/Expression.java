package net.akami.atosym.expression;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.ExpressionDisplayer;

public abstract class Expression<V> implements MathObject {

    protected V value;
    private DisplayerVisitor displayer;

    public Expression(V value) {
        this.value = value;
        this.displayer = new ExpressionDisplayer(value);
    }

    public V getValue() {
        return value;
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }

    @Override
    public int priority() {
        return 9;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Expression)) {
            return false;
        }
        return value.equals(((Expression) obj).value);
    }

    @Override
    public String toString() {
        return getDisplayer().accept(InfixNotationDisplayable.EMPTY_INSTANCE);
    }
}
