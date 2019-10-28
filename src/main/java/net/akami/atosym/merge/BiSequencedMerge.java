package net.akami.atosym.merge;

import net.akami.atosym.merge.BiSequencedMerge.BiListContainer;
import net.akami.atosym.merge.property.OverallMergeProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface BiSequencedMerge<T> extends Merge<List<T>, BiListContainer<T>> {

    @Override
    default List<OverallMergeProperty<List<T>, BiListContainer<T>>> generateOverallProperties(List<T> p1, List<T> p2) {
        return Collections.emptyList();

    }

    @Override
    default BiListContainer<T> merge(List<T> p1, List<T> p2, boolean selfMerge) {
        List<T> list0 = new ArrayList<>();
        List<T> list1 = new ArrayList<>();

        return new BiListContainer<>(list0, list1);
    }

    class BiListContainer<T> {

        private List<T> firstList;
        private List<T> secondList;

        public BiListContainer(List<T> firstList, List<T> secondList) {
            this.firstList = firstList;
            this.secondList = secondList;
        }

        public List<T> getFirstList() {
            return firstList;
        }

        public List<T> getSecondList() {
            return secondList;
        }
    }
}
