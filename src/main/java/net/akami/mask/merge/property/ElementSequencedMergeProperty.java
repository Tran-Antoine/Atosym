package net.akami.mask.merge.property;

import net.akami.mask.merge.SequencedMergeProperty;

import java.util.List;

public abstract class ElementSequencedMergeProperty<T> extends SequencedMergeProperty<T, List<T>> {

    protected ElementSequencedMergeProperty(T p1, T p2, boolean startOverRequest) {
        super(p1, p2, startOverRequest);
    }
}
