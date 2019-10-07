package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.merge.property.sum.ChainSumProperty;
import net.akami.atosym.merge.property.sum.IdenticalVariablesSumProperty;
import net.akami.atosym.merge.property.sum.NumericSumProperty;

import java.util.Arrays;
import java.util.List;

public class MonomialAdditionMerge implements SequencedMerge<MathObject> {

    protected MaskContext context;

    public MonomialAdditionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<ElementSequencedMergeProperty<MathObject>> generateElementProperties(MathObject p1, MathObject p2) {
        return Arrays.asList(
                new NumericSumProperty(p1, p2, this.context),
                new IdenticalVariablesSumProperty(p1, p2, this.context),
                new ChainSumProperty(p1, p2, this.context)
        );
    }

}
