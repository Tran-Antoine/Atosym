package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.CosineMathObject;
import net.akami.atosym.expression.MathObject;

import java.util.List;
import java.util.function.Function;

public class CosineOperator extends TrigonometryOperator {

    public CosineOperator(MaskContext context) {
        super("cos", context);
    }

    @Override
    protected Function<Double, Double> getOperation() {
        return Math::cos;
    }

    @Override
    protected MathObject toObject(List<MathObject> input) {
        return new CosineMathObject(input, context);
    }
}
