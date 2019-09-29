package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.VariableExpression;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.merge.property.OverallSequencedMergeProperty;

import java.util.Collections;
import java.util.List;

public class VariableCombination implements SequencedMerge<VariableExpression> {

    private MaskContext context;

    public VariableCombination(MaskContext context) {
        this.context = context;
    }

    // For now it only has one property
    @Override
    public List<ElementSequencedMergeProperty<VariableExpression>> generateElementProperties(VariableExpression p1, VariableExpression p2) {
        return Collections.singletonList(
                null//new BaseEquivalenceMultProperty(p1, p2, context)
        );
    }

    @Override
    public List<OverallSequencedMergeProperty<VariableExpression>> generateOverallProperties(List<VariableExpression> p1, List<VariableExpression> p2) {
        return Collections.singletonList(
                null//new FractionCombinationProperty(p1, p2, context)
        );
    }
}
