package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.merge.property.*;

import java.util.Collections;
import java.util.List;

public class VariableCombination implements SequencedMerge<Variable> {

    private MaskContext context;

    public VariableCombination(MaskContext context) {
        this.context = context;
    }

    // For now it only has one property
    @Override
    public List<ElementSequencedMergeProperty<Variable>> generateElementProperties(Variable p1, Variable p2) {
        return Collections.singletonList(
                new BaseEquivalenceMultProperty(p1, p2, context)
        );
    }

    @Override
    public List<OverallSequencedMergeProperty<Variable>> generateOverallProperties(List<Variable> p1, List<Variable> p2) {
        return Collections.singletonList(
                new FractionCombinationProperty(p1, p2, context)
        );
    }
}
