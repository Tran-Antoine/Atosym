package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.merge.property.FairElementMergeProperty;
import net.akami.atosym.merge.property.mult.*;
import net.akami.atosym.sorting.SortingRules;
import net.akami.atosym.utils.NumericUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultiplicationMerge extends FairSequencedMerge<MathObject> {

    private MaskContext context;
    private SortingRules rules;

    public MultiplicationMerge(MaskContext context) {
        this.context = context;
        this.rules = context.getSortingRules(MathObjectType.MULT);
    }

    @Override
    public List<FairElementMergeProperty<MathObject>> loadPropertiesFrom(MathObject p1, MathObject p2) {
        return Arrays.asList(
                new NumericMultProperty(p1, p2, context),
                new VariableSquaredProperty(p1, p2, context),
                new ChainMultProperty(p1, p2, context),
                new MultOfSumProperty(p1, p2, context),
                new IdenticalBaseProperty(p1, p2, context),
                new MultOfFractionsProperty(p1, p2, context)
        );
    }

    @Override
    public List<MathObject> loadFinalResult() {
        return super.loadFinalResult()
                .stream()
                .filter(NumericUtils::isNotOne)
                .collect(Collectors.toList());
    }
}
