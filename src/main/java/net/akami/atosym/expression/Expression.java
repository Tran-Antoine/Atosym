package net.akami.atosym.expression;

public abstract class Expression<V> implements MathObject {

    protected V value;

    public Expression(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String display() {
        return value.toString();
    }

    @Override
    public int priority() {
        return 9;
    }
    
    @Override
    public int compareTo(MathObject o) {
        return 0;
    }
}
