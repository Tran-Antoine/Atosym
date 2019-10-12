package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.merge.property.global.NumericOperation;

public class NumberExpression extends Expression<Float> {

    public static final NumberExpression NEUTRAL_MULT_FACTOR = new NumberExpression(1f);

    public NumberExpression(String value) {
        this(Float.parseFloat(value));
    }

    public NumberExpression(Float value) {
        super(value);
    }

    public NumberExpression(NumberExpression a, NumberExpression b, NumericOperation function, MaskContext context) {
        super(function.compute(a.getValue(), b.getValue(), context));
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.NUMBER;
    }
}
