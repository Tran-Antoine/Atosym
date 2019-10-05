package net.akami.atosym.expression;

import java.util.function.BiFunction;

public class NumberExpression extends Expression<Float> {

    public NumberExpression(String value) {
        this(Float.parseFloat(value));
    }

    public NumberExpression(Float value) {
        super(value);
    }

    public NumberExpression(NumberExpression a, NumberExpression b, BiFunction<Float, Float, Float> function) {
        super(function.apply(a.getValue(), b.getValue()));
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.NUMBER;
    }

    @Override
    public String toString() {
        return display();
    }
}
