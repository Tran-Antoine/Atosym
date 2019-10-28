package net.akami.atosym.merge.property;

import net.akami.atosym.merge.MergeProperty;

import java.util.List;

public abstract class BiElementMergeProperty<P> extends MergeProperty<P> implements RestartApplicant {

    private boolean startOverRequested;

    public BiElementMergeProperty(P p1, P p2, boolean startOverRequested) {
        super(p1, p2);
        this.startOverRequested = startOverRequested;
    }

    public abstract void blendResult(List<P> listA, List<P> listB);

    @Override
    public boolean isRestartRequired() {
        return startOverRequested;
    }
}
