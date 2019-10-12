package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.merge.property.sum.ChainSumProperty;
import net.akami.atosym.merge.property.sum.IdenticalVariablesSumProperty;
import net.akami.atosym.merge.property.sum.NumericSumProperty;
import net.akami.atosym.merge.property.sum.SumOfMultProperty;

import java.util.Arrays;
import java.util.List;

public class AdditionMerge implements SequencedMerge<MathObject> {

    protected MaskContext context;

    public AdditionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<ElementSequencedMergeProperty<MathObject>> generateElementProperties(MathObject p1, MathObject p2) {
        return Arrays.asList(
                new NumericSumProperty(p1, p2, this.context),
                new IdenticalVariablesSumProperty(p1, p2, this.context),
                new ChainSumProperty(p1, p2, this.context),
                new SumOfMultProperty(p1, p2, context)
        );
    }

}
