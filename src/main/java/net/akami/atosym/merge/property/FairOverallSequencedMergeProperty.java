package net.akami.atosym.merge.property;

import java.util.List;

public abstract class FairOverallSequencedMergeProperty<T> extends FairOverallMergeProperty<List<T>> {

    public FairOverallSequencedMergeProperty(List<T> p1, List<T> p2) {
        super(p1, p2);
    }
}
