package net.akami.mask.merge;

import net.akami.mask.merge.property.ElementSequencedMergeProperty;
import net.akami.mask.merge.property.FairOverallMergeProperty;
import net.akami.mask.merge.property.OverallSequencedMergeProperty;

import java.util.*;
import java.util.stream.Collectors;

public interface SequencedMerge<T> extends FairMerge<List<T>, OverallSequencedMergeProperty<T>> {

    List<ElementSequencedMergeProperty<T>> generateElementProperties(T p1, T p2);

    @Override
    default List<OverallSequencedMergeProperty<T>> generateOverallProperties(List<T> p1, List<T> p2) {
        return Collections.emptyList();
    }


    default Optional<ElementSequencedMergeProperty<T>> prepare(T p1, T p2) {
        for(ElementSequencedMergeProperty<T> property : generateElementProperties(p1, p2)) {
            if(property.isSuitable()) return Optional.of(property);
        }
        return Optional.empty();
    }

    /**
     * Constructs a list from the subtypes of the handled type. However, you can not expect to retrieve a list
     * containing only objects having the same type as the parameter types. Therefore, a {@code SequenceMerge<Object>}
     * for instance will always return a list of objects, since every object added into the constructed list is not
     * guaranteed to be anything else than an Object.
     * @param l1 the first list to merge
     * @param l2 the second list to merge
     * @param selfMerge whether the merge is proceeded with a single list or not
     * @param <S> the subtype
     * @return a merge of the two lists
     */
    default <S extends T> List<T> subtypeMerge(List<S> l1, List<S> l2, boolean selfMerge) {
        return subtypeMerge(l1, l2, selfMerge, 0);
    }

    default <S extends T> List<T> subtypeMerge(List<S> l1, List<S> l2, boolean selfMerge, int initialCapacity) {
        List<T> copy1 = new ArrayList<>(l1);
        List<T> copy2 = new ArrayList<>(l2);
        return merge(copy1, copy2, selfMerge, initialCapacity);
    }
    /**
     * Should most of the time not be overridden through subclasses, since the behavior corresponds to list merging
     * @param p1 the first list to merge
     * @param p2 the second list to merge
     * @param selfMerge whether the merge is proceeded with a single list or not
     * @return a merge of the two lists
     */
    @Override
    default List<T> merge(List<T> p1, List<T> p2, boolean selfMerge) {
        return merge(p1, p2, selfMerge, 0);
    }

    default List<T> merge(List<T> l1, List<T> l2, boolean selfMerge, int initialCapacity) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        boolean requestsStartingOver = false;
        List<T> finalResult = new ArrayList<>(initialCapacity);

        for(FairOverallMergeProperty<List<T>> overallProperty : generateOverallProperties(l1, l2)) {
            if(overallProperty.isSuitable()) {
                // Overall properties are capable of creating a full result by themselves
                return overallProperty.computeResult();
            }
        }

        int i = 0;
        for (T element : l1) {
            if (element == null) {
                i++;
                continue;
            }
            int j = 0;
            for (T element2 : l2) {

                if (selfMerge && i == j) { j++; continue; }
                if (element2 == null) { j++; continue; }

                Optional<ElementSequencedMergeProperty<T>> optionalMergeData = prepare(element, element2);
                if (optionalMergeData.isPresent()) {
                    ElementSequencedMergeProperty<T> mergeData = optionalMergeData.get();

                    if(selfMerge) requestsStartingOver = true;

                    compatibilityFound(l1, l2, i, j, finalResult, mergeData);

                    if(mergeData.isStartOverRequested()) {
                        requestsStartingOver = true;
                    }
                    break;
                }
                j++;
            }
            i++;
        }
        finalResult.addAll(l1.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        if (!selfMerge)
            finalResult.addAll(l2.stream().filter(Objects::nonNull).collect(Collectors.toList()));

        if(requestsStartingOver) {
            return merge(finalResult, finalResult, true);
        }

        return finalResult;
    }

    default void compatibilityFound(List<T> l1, List<T> l2, int i, int j, List<T> constructed,
                                    ElementSequencedMergeProperty<T> compatibility) {
        l1.set(i, null);
        l2.set(j, null);
        compatibility.blendResult(constructed);
    }
}
