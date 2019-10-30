package net.akami.atosym.merge;

import net.akami.atosym.merge.property.SimpleElementMergeProperty;
import net.akami.atosym.merge.property.OverallMergeProperty;
import net.akami.atosym.merge.property.OverallSequencedMergeProperty;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A merge concerning sequences of elements. <br>
 * The SimpleSequencedMerge behavior is a slightly more complex merge than usual merges. As every normal merge, it first
 * tries to compute a full list from the {@link OverallSequencedMergeProperty}s handled. However, if no property matches
 * the two lists (which is likely to happen), both lists will be browsed to try to merge elements. <br>
 * Each element of the first list will be compared to each element of the second list. If an {@link SimpleElementMergeProperty}
 * is found to be suitable for the two elements, one or several results will be added to a new list which will be the merge result.
 * Elements which couldn't be merged at all (elements to which no property applied) will eventually be added to the list. <br>
 * See {@link #merge(List, List, boolean)} or {@link #merge(List, List, boolean)} for further information.
 *
 * @param <T> the type of elements composing the sequences
 * @author Antoine Tran
 */
public abstract class FairSequencedMerge<T> implements SequencedMerge<T, List<T>, SimpleElementMergeProperty<T>> {

    private List<T> l1;
    private List<T> l2;
    private List<T> result;
    private boolean selfMerge;

    public FairSequencedMerge() {
        this.result = new ArrayList<>();
    }

    /**
     * Constructs a list from the subtypes of the handled type. However, you can not expect to retrieve a list
     * containing only objects having the same type as the parameter types. Therefore, a {@code SequenceMerge<Object>}
     * for instance will always return a list of objects, since every item added into the constructed list is not
     * guaranteed to be anything else than an Object. <br>
     * See {@link #merge(List, List, boolean)} for further information
     * @param l1 the first list to merge
     * @param l2 the second list to merge
     * @param selfMerge whether the merge is proceeded with a single list or not
     * @param <S> the subtype
     * @return a merge of the two lists
     */
    public <S extends T> List<T> subtypeMerge(List<S> l1, List<S> l2, boolean selfMerge) {
        return subtypeMerge(l1, l2, selfMerge);
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
    public MergeFlowModification<T> associate(SimpleElementMergeProperty<T> property) {
        property.blendResult(result);
        return SequencedMerge.super::nullifyElements;
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

    @Override
    public List<T> andThenMerge() {
        return SequencedMerge.super.merge(result, result, true);
    }



}
