package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.SimpleElementMergeProperty;
import net.akami.atosym.merge.property.mult.*;

import java.util.Arrays;
import java.util.List;

public class MultiplicationMerge implements SimpleSequencedMerge<MathObject> {

    private MaskContext context;

    public MultiplicationMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<SimpleElementMergeProperty<MathObject>> generateElementProperties(MathObject p1, MathObject p2) {
        return Arrays.asList(
                new NumericMultProperty(p1, p2, context),
                new VariableSquaredProperty(p1, p2),
                new ChainMultProperty(p1, p2, context),
                new MultOfSumProperty(p1, p2, context),
                new IdenticalBaseProperty(p1, p2, context),
                new MultOfFractionsProperty(p1, p2, context)
        );
    }
}
