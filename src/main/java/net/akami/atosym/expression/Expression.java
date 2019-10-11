package net.akami.atosym.expression;

import net.akami.atosym.display.visitor.ExpressionDisplayer;

public abstract class Expression<V> implements MathObject {

    protected V value;
    private ExpressionDisplayer displayer;

    public Expression(V value) {
        this.value = value;
        this.displayer = new ExpressionDisplayer(value);
    }

    public V getValue() {
        return value;
    }

    @Override
    public ExpressionDisplayer getDisplayer() {
        return displayer;
    }

    @Override
    public int priority() {
        return 9;
    }
    
    @Override
    public int compareTo(MathObject o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Expression)) {
            return false;
        }
        return value.equals(((Expression) obj).value);
    }
}
