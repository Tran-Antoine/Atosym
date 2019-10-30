package net.akami.atosym.merge;

import net.akami.atosym.merge.BiSequencedMerge.BiListContainer;
import net.akami.atosym.merge.property.BiElementMergeProperty;

import java.util.List;

public abstract class BiSequencedMerge<T> implements SequencedMerge<T, BiListContainer, BiElementMergeProperty<T>> {

    protected List<T> listA;
    protected List<T> listB;

    @Override
    public BiListContainer merge(List<T> l1, List<T> l2, boolean selfMerge) {
        this.listA = l1;
        this.listB = l2;
        return andThenMerge(l1, l2, selfMerge);
    }

    protected BiListContainer andThenMerge(List<T> l1, List<T> l2, boolean selfMerge) {
        return SequencedMerge.super.merge(l1, l2, selfMerge);
    }


    public class BiListContainer {

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
