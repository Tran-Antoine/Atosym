package net.akami.atosym.merge;

import net.akami.atosym.merge.property.RestartApplicant;
import net.akami.atosym.merge.property.OverallMergeProperty;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    default List<OverallMergeProperty<List<T>, R>> generateOverallProperties(List<T> l1, List<T> l2) {
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
    default void nullifyElements(List<T> l1, int i, List<T> l2, int j) {
        l1.set(i, null);
        l2.set(j, null);
    }

    MergeFlowModification<T> associate(PROP property);
    R andThenMerge();
    R loadFinalResult();

    interface MergeFlowModification<T> {
        void thenModify(List<T> l1, int i, List<T> l2, int j);
    }
}
