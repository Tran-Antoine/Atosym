package net.akami.atosym.merge;

import net.akami.atosym.merge.property.OverallMergeProperty;
import net.akami.atosym.merge.property.FairOverallSequencedMergeProperty;
import net.akami.atosym.merge.property.RestartApplicant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A merge concerning sequences of elements. <br>
 * The SequencedMerge behavior is a slightly more complex merge than usual merges. As every normal merge, it first
 * tries to compute a full result from the {@link FairOverallSequencedMergeProperty}s handled. However, if no property matches
 * the two lists (which is likely to happen), both lists will be browsed to try to merge elements. <br>
 * Each element of the first list will be compared to each element of the second list. If a {@link MergeProperty}
 * is found to be suitable for the two elements, an action implemented in subclasses will occur. These actions usually contribute to building
 * an object which will be the final result returned.
 * By default, elements which couldn't be merged at all (elements to which no property applied) won't be used nor stored. <br>
 * See {@link #merge(List, List, boolean)} or {@link #merge(List, List, boolean)} for further information.
 *
 * @param <T> the type of elements composing the sequences
 * @author Antoine Tran
 */
public interface SequencedMerge<T, R, PROP extends MergeProperty<T> & RestartApplicant> extends Merge<List<T>, R> {

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
    List<PROP> loadPropertiesFrom(T p1, T p2);

    @Override
    default List<? extends OverallMergeProperty<List<T>, R>> generateOverallProperties(List<T> l1, List<T> l2) {
        return Collections.emptyList();
    }

    /**
     * Method that should usually not be used outside this class. Java 8 does not support private methods in interfaces. <br>
     * @param p1 the element from the first list to compare
     * @param p2 the element from the second list to compare
     * @return an optional containing the first suitable property for the two elements. If no property matches the two elements,
     * an empty optional is returned
     */
    default Optional<PROP> prepare(T p1, T p2) {
        return loadPropertiesFrom(p1, p2)
                .stream()
                .filter(PROP::prepare)
                .findAny();
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
    default <S extends T> R subtypeMerge(List<S> l1, List<S> l2, boolean selfMerge) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2), selfMerge);
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
    default R merge(List<T> l1, List<T> l2, boolean selfMerge) {

        boolean requestsStartingOver = false;

        Optional<R> optionalResult = Merge.super.resultFromProperties(l1, l2);
        if(optionalResult.isPresent()) return optionalResult.get();

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

                Optional<PROP> optionalMergeData = prepare(element, element2);
                if (optionalMergeData.isPresent()) {
                    PROP property = optionalMergeData.get();
                    associate(property).thenModify(l1, i, l2, j);
                    if(selfMerge || property.isRestartRequired()) {
                        requestsStartingOver = true;
                    }
                    break;
                }
                j++;
            }
            i++;
        }
        if(requestsStartingOver) {
            return andThenMerge();
        }
        return loadFinalResult();
    }

    /**
     * Action to perform when a property matches two elements. <br>
     * This method should usually not be redefined, since the logical behavior is to remove the elements from
     * the former lists, and add the computed one to the constructed list.
     * @param l1 the first former list
     * @param l2 the second former list
     * @param i the index of the element present in {@code l1}
     * @param j the index of the element present in {@code l2}
     */
    static <T> void nullifyElements(List<T> l1, int i, List<T> l2, int j) {
        l1.set(i, null);
        l2.set(j, null);
    }

    MergeFlowModification<T> associate(PROP property);
    R andThenMerge();
    R loadFinalResult();

    interface MergeFlowModification<T> {

        static <T> MergeFlowModification<T> none() {
            return ((l1, i, l2, j) -> {});
        }

        void thenModify(List<T> l1, int i, List<T> l2, int j);
    }
}
