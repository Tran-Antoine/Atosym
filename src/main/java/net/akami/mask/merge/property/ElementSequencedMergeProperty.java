package net.akami.mask.merge.property;

import net.akami.mask.merge.MergeProperty;

import java.util.List;

public abstract class ElementSequencedMergeProperty<P> extends MergeProperty<P> {

    public abstract void blendResult(List<P> constructed);

    public ElementSequencedMergeProperty(P p1, P p2, boolean startOverRequested) {
        super(p1, p2, startOverRequested);
    }
}
