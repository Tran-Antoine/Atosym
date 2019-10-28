package net.akami.atosym.merge;

import net.akami.atosym.merge.BiSequencedMerge.BiListContainer;
import net.akami.atosym.merge.property.BiElementMergeProperty;

import java.util.List;

public interface BiSequencedMerge<T> extends SequencedMerge<T, BiListContainer<T>, BiElementMergeProperty<T>> {

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
