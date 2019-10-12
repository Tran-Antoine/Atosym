package net.akami.atosym.merge;

import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.merge.property.OverallMergeProperty;
import net.akami.atosym.merge.property.OverallSequencedMergeProperty;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A merge concerning sequences of elements. <br>
 * The SequencedMerge behavior is a slightly more complex merge than usual merges. As every normal merge, it first
 * tries to compute a full list from the {@link OverallSequencedMergeProperty}s handled. However, if no property matches
 * the two lists (which is likely to happen), both lists will be browsed to try to merge elements. <br>
 * Each element of the first list will be compared to each element of the second list. If an {@link ElementSequencedMergeProperty}
 * is found suitable for the two elements, one or several results will be added to a new list which will be the merge result.
 * Elements which couldn't be merged at all (elements to which no property applied) will eventually be added to the list. <br>
 * See {@link #merge(List, List, boolean)} or {@link #merge(List, List, boolean, int)} for further information.
 *
 * @param <T> the type of elements composing the sequences
 * @author Antoine Tran
 */
public interface SequencedMerge<T> extends FairMerge<List<T>, OverallSequencedMergeProperty<T>> {

    /**
     * Generates a list containing the element properties. Contrary to {@link OverallMergeProperty}s, they don't directly
     * apply to the input given (being the two sequences of elements), but to elements forming the lists. <br>
     * Every time two elements are compared, a new list of properties must be generated. The reason for that is that these properties
     * might handle different fields that depend on the constructor parameters (the two elements). Using setters instead of
     * re-instantiating new properties is also not recommended, since a property might recursively use itself, and change
     * the value of the elements before the end of the calculation of the result. If you are guaranteed that recursive usage will not
     * happen, you might consider adding setters to your properties instead and always return the same list.
     * @param p1 the element from the first list to compare
     * @param p2 the element from the second list to compare
     * @return a list of properties suiting {@code p1} and {@code p2}
     */
    List<ElementSequencedMergeProperty<T>> generateElementProperties(T p1, T p2);

    /**
     * @return an empty list by default. In most of the cases, sequenced merges don't have any overall properties
     */
    @Override
    default List<OverallSequencedMergeProperty<T>> generateOverallProperties(List<T> p1, List<T> p2) {
        return Collections.emptyList();
    }

    /**
     * Method that should usually not be used outside this class. Java 8 does not support private methods in interfaces. <br>
     * @param p1 the element from the first list to compare
     * @param p2 the element from the second list to compare
     * @return an optional containing the first suitable property for the two elements. If no property matches the two elements,
     * an empty optional is returned
     */
    default Optional<ElementSequencedMergeProperty<T>> prepare(T p1, T p2) {
        for(ElementSequencedMergeProperty<T> property : generateElementProperties(p1, p2)) {
            if(property.prepare()) return Optional.of(property);
        }
        return Optional.empty();
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
    default <S extends T> List<T> subtypeMerge(List<S> l1, List<S> l2, boolean selfMerge) {
        return subtypeMerge(l1, l2, selfMerge, 0);
    }

    /**
     * Constructs a list from the subtypes of the handled type. However, you can not expect to retrieve a list
     * containing only objects having the same type as the parameter types. Therefore, a {@code SequenceMerge<Object>}
     * for instance will always return a list of objects, since every item added into the constructed list is not
     * guaranteed to be anything else than an Object. <br>
     * See {@link #merge(List, List, boolean, int)} for further information
     * @param l1 the first list to merge
     * @param l2 the second list to merge
     * @param selfMerge whether the merge is proceeded with a single list or not
     * @param initialCapacity the initial capacity of the new list
     * @param <S> the subtype
     * @return a merge of the two lists
     */
    default <S extends T> List<T> subtypeMerge(List<S> l1, List<S> l2, boolean selfMerge, int initialCapacity) {
        List<T> copy1 = new ArrayList<>(l1);
        List<T> copy2 = new ArrayList<>(l2);
        return merge(copy1, copy2, selfMerge, initialCapacity);
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
    default List<T> merge(List<T> l1, List<T> l2, boolean selfMerge) {
        return merge(l1, l2, selfMerge, 0);
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
     * @param initialCapacity the initial capacity of the new list, 0 if not specified
     * @return a merge of the two lists
     */
    default List<T> merge(List<T> l1, List<T> l2, boolean selfMerge, int initialCapacity) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        boolean requestsStartingOver = false;
        List<T> finalResult = new ArrayList<>(initialCapacity);

        for(FairOverallMergeProperty<List<T>> overallProperty : generateOverallProperties(l1, l2)) {
            if(overallProperty.prepare()) {
                // Overall properties are capable of creating a full result by themselves
                return overallProperty.rawComputeResult();
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

    /**
     * Action to perform when a property matches two elements. <br>
     * This method should usually not be redefined, since the logical behavior is to remove the elements from
     * the former lists, and add the computed one to the constructed list.
     * @param l1 the first former list
     * @param l2 the second former list
     * @param i the index of the element present in {@code l1}
     * @param j the index of the element present in {@code l2}
     * @param constructed the constructed list
     * @param compatibility the property suitable for the two elements
     */
    default void compatibilityFound(List<T> l1, List<T> l2, int i, int j, List<T> constructed,
                                    ElementSequencedMergeProperty<T> compatibility) {
        l1.set(i, null);
        l2.set(j, null);
        compatibility.blendResult(constructed);
    }
}
