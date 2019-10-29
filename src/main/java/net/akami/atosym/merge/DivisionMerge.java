package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.BiElementMergeProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DivisionMerge implements BiSequencedMerge<MathObject> {

    private MaskContext context;
    private List<MathObject> numerator;
    private List<MathObject> denominator;

    public DivisionMerge(MaskContext context) {
        this.context = context;
        this.numerator = new ArrayList<>();
        this.denominator = new ArrayList<>();
    }

    @Override
    public List<BiElementMergeProperty<MathObject>> loadPropertiesFrom(MathObject p1, MathObject p2) {
        return Arrays.asList(
        );
    }

    @Override
    public MergeFlowModification<MathObject> associate(BiElementMergeProperty<MathObject> property) {
        property.blendResult(numerator, denominator);
        return BiSequencedMerge.super::nullifyElements;
    }

    @Override
    public BiListContainer<MathObject> andThenMerge() {
        return merge(numerator, denominator, false);
    }

    @Override
    public BiListContainer<MathObject> loadFinalResult() {
        return new BiListContainer<>(numerator, denominator);
    }
}
