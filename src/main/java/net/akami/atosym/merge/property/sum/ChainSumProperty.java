package net.akami.atosym.merge.property.sum;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.merge.AdditionMerge;
import net.akami.atosym.merge.SimpleSequencedMerge;
import net.akami.atosym.merge.property.global.ChainOperationProperty;
import net.akami.atosym.utils.NumericUtils;

import java.util.function.Predicate;

public class ChainSumProperty extends ChainOperationProperty {

    public ChainSumProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, context, false);
    }

    @Override
    protected MathObjectType getWorkingType() {
        return MathObjectType.SUM;
    }

    @Override
    protected SimpleSequencedMerge<MathObject> generateMergeTool(MaskContext context) {
        return new AdditionMerge(context);
    }

    @Override
    protected Predicate<MathObject> significantElementCondition() {
        return NumericUtils::isNotZero;
    }
}
