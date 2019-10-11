package net.akami.atosym.operator;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class TrigonometryOperator extends MathOperator implements AngleUnitDependent {

    public TrigonometryOperator(String name) {
        super(Collections.singletonList(name), 1);
    }

    @Override
    protected MathObject operate(List<MathObject> input) {
        MathObject unique = input.get(0);

        if(unique.getType() == MathObjectType.NUMBER) {
            NumberExpression exp = (NumberExpression) unique;
            double value = getOperation().apply(exp.getValue().doubleValue());
            return new NumberExpression((float) value);
        }

        return toObject(input);
    }

    protected abstract Function<Double, Double> getOperation();
    protected abstract MathObject toObject(List<MathObject> input);
}
