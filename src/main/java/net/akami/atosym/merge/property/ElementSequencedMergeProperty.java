package net.akami.atosym.merge.property;

import net.akami.atosym.merge.MergeProperty;

import java.util.List;

public abstract class ElementSequencedMergeProperty<P> extends MergeProperty<P> {

    private boolean startOverRequested;

    public ElementSequencedMergeProperty(P p1, P p2, boolean startOverRequested) {
        super(p1, p2);
        this.startOverRequested = startOverRequested;
    }

    public abstract void blendResult(List<P> constructed);

    public boolean isStartOverRequested() {
        return startOverRequested;
    }
}
