package net.akami.atosym.merge;

import net.akami.atosym.merge.property.RestartApplicant;
import net.akami.atosym.merge.property.OverallMergeProperty;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface SequencedMerge<T, R, PROP extends MergeProperty<T> & RestartApplicant> extends Merge<List<T>, R> {

    List<PROP> loadPropertiesFrom(T p1, T p2);

    @Override
    default List<OverallMergeProperty<List<T>, R>> generateOverallProperties(List<T> l1, List<T> l2) {
        return Collections.emptyList();
    }

    default Optional<PROP> prepare(T p1, T p2) {
        for(PROP property : loadPropertiesFrom(p1, p2)) {
            if(property.prepare()) return Optional.of(property);
        }
        return Optional.empty();
    }

    @Override
    default R merge(List<T> l1, List<T> l2, boolean selfMerge) {

        boolean requestsStartingOver = false;

        Optional<R> potentialResult = resultFromProperties(l1, l2);
        if(potentialResult.isPresent()) return potentialResult.get();

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
                    if(selfMerge) { requestsStartingOver = true; }
                    associate(property).modify(l1, i, l2, j);
                    if(property.isRestartRequired()) {
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
        void modify(List<T> l1, int i, List<T> l2, int j);
    }
}
