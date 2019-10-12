package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.merge.property.mult.ChainMultProperty;
import net.akami.atosym.merge.property.mult.IdenticalBaseProperty;
import net.akami.atosym.merge.property.mult.NumericMultProperty;
import net.akami.atosym.merge.property.mult.VariableSquaredProperty;

import java.util.Arrays;
import java.util.List;

public class MonomialMultiplicationMerge implements SequencedMerge<MathObject> {

    private MaskContext context;

    public MonomialMultiplicationMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<ElementSequencedMergeProperty<MathObject>> generateElementProperties(MathObject p1, MathObject p2) {
        return Arrays.asList(
                new NumericMultProperty(p1, p2, context),
                new VariableSquaredProperty(p1, p2),
                new ChainMultProperty(p1, p2, context),
                new IdenticalBaseProperty(p1, p2, context)
        );
    }
}
