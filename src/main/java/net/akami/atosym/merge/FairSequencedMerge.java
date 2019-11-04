package net.akami.atosym.merge;

import net.akami.atosym.merge.property.FairElementMergeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FairSequencedMerge<T> implements SequencedMerge<T, List<T>, FairElementMergeProperty<T>> {

    private List<T> l1;
    private List<T> l2;
    private List<T> result;
    private boolean selfMerge;

    public FairSequencedMerge() {
        this.result = new ArrayList<>();
    }

    /**
     * Merges the two lists. <br>
     * Basically, compare each element of the first list with each element of the second one. If a result can be computed
     * out of these two elements, they will be removed and the computed result will be added into the constructed list. <br>
     * If the same list is passed in both list parameters, elements with the same index won't be compared.
     * Because the elements are removed as you go, an element that found another compatible element won't be merged any more. <br>
     * After all comparisons, the elements which could not be merged will be added into the constructed list. <br>
     *
     * Note that the method should most of the time not be overridden through subclasses, since the behavior corresponds to list merging
     * @param l1 the first list to merge
     * @param l2 the second list to merge
     * @param selfMerge whether the merge is proceeded with a single list or not
     * @return a merge of the two lists
     */
    @Override
    public List<T> merge(List<T> l1, List<T> l2, boolean selfMerge) {
        this.l1 = l1;
        this.l2 = l2;
        this.selfMerge = selfMerge;
        return SequencedMerge.super.merge(l1, l2, selfMerge);
    }

    @Override
    public MergeFlowModification<T> associate(FairElementMergeProperty<T> property) {
        property.blendResult(result);
        return SequencedMerge::nullifyElements;
    }

    @Override
    public List<T> loadFinalResult() {
        Function<List<T>, Boolean> addAllFunction = (b) -> result.addAll(b
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        addAllFunction.apply(l1);
        if (!selfMerge) {
            addAllFunction.apply(l2);
        }
        return result;
    }
}
