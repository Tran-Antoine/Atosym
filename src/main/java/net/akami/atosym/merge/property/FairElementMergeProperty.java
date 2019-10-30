package net.akami.atosym.merge.property;

import net.akami.atosym.merge.MergeProperty;

import java.util.List;

public abstract class FairElementMergeProperty<P> extends MergeProperty<P> implements RestartApplicant {

    private boolean startOverRequested;

    public FairElementMergeProperty(P p1, P p2, boolean startOverRequested) {
        super(p1, p2);
        this.startOverRequested = startOverRequested;
    }

    public abstract void blendResult(List<P> constructed);

    @Override
    public boolean isRestartRequired() {
        return startOverRequested;
    }
}
