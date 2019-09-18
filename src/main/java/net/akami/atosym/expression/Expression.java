package net.akami.atosym.expression;

public class Expression<V> implements MathObject {

    private V value;

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
    public MathObject operate() {
        return this;
    }
}
