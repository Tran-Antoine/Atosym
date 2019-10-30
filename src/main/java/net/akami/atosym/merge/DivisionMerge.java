package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.BiElementMergeProperty;
import net.akami.atosym.merge.property.BiOverallSequencedMergeProperty;
import net.akami.atosym.merge.property.division.DivisionOfMultiplicationProperty;
import net.akami.atosym.merge.property.division.IdenticalElementsProperty;
import net.akami.atosym.merge.property.division.IdenticalNumAndDenProperty;
import net.akami.atosym.merge.property.division.NumericalDivisionProperty;
import net.akami.atosym.utils.NumericUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DivisionMerge extends BiSequencedMerge<MathObject> {

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
                new IdenticalElementsProperty(p1, p2),
                new NumericalDivisionProperty(p1, p2, context),
                new DivisionOfMultiplicationProperty(p1, p2)
        );
    }

    @Override
    public List<BiOverallSequencedMergeProperty<MathObject>> generateOverallProperties(List<MathObject> l1, List<MathObject> l2) {
        return Collections.singletonList(new IdenticalNumAndDenProperty(l1, l2, this));
    }

    @Override
    public MergeFlowModification<MathObject> associate(BiElementMergeProperty<MathObject> property) {
        property.blendResult(numerator, denominator);
        return SequencedMerge::nullifyElements;
    }

    @Override
    public BiListContainer andThenMerge() {
        return super.andThenMerge(numerator, denominator, false);
    }

    @Override
    public BiListContainer loadFinalResult() {
        return new BiListContainer(filter(numerator, listA), filter(denominator, listB));
    }

    private List<MathObject> filter(List<MathObject> target, List<MathObject> leftOver) {
        target.addAll(leftOver);
        return target
                .stream()
                .filter(Objects::nonNull)
                .filter(NumericUtils::isNotOne)
                .collect(Collectors.toList());
    }
}
