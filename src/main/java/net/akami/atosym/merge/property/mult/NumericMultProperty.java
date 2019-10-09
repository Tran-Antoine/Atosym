package net.akami.atosym.merge.property.mult;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.utils.NumericUtils;

import java.util.List;
import java.util.function.BiFunction;

public class NumericMultProperty extends ElementSequencedMergeProperty<MathObject> {

    private MaskContext context;

    public NumericMultProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {
        NumberExpression numericA = (NumberExpression) p1;
        NumberExpression numericB = (NumberExpression) p2;

        BiFunction<Float, Float, Float> func = (a, b) -> NumericUtils.mult(a, b, context);
        constructed.add(new NumberExpression(numericA, numericB, func));
    }

    @Override
    public boolean isSuitable() {
        return p1.getType() == MathObjectType.NUMBER && p2.getType() == MathObjectType.NUMBER;
    }
}
