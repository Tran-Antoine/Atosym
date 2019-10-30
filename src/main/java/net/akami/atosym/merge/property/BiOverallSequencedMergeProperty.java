package net.akami.atosym.merge.property;

import net.akami.atosym.merge.BiSequencedMerge.BiListContainer;

import java.util.List;

public abstract class BiOverallSequencedMergeProperty<T> extends OverallMergeProperty<List<T>, BiListContainer> {
    public BiOverallSequencedMergeProperty(List<T> p1, List<T> p2) {
        super(p1, p2);
    }
}
