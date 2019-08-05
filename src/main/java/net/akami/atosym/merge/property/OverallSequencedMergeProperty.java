package net.akami.atosym.merge.property;

import java.util.List;

public abstract class OverallSequencedMergeProperty<T> extends FairOverallMergeProperty<List<T>> {

    public OverallSequencedMergeProperty(List<T> p1, List<T> p2, boolean startOverRequested) {
        super(p1, p2, startOverRequested);
    }
}
