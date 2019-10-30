package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.TangentMathObject;

import java.util.List;
import java.util.function.Function;

public class TangentOperator extends TrigonometryOperator {

    public TangentOperator(MaskContext context) {
        super("tan", context);
    }

    @Override
    protected Function<Double, Double> getOperation() {
        return Math::tan;
    }

    @Override
    protected MathObject toObject(List<MathObject> input) {
        return new TangentMathObject(input, context);
    }
}
