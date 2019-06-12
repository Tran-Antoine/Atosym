package net.akami.mask.merge;

public abstract class SequencedMergeProperty<P, C> {

    protected P p1;
    protected P p2;
    protected boolean startOverRequested;

    public SequencedMergeProperty(P p1, P p2, boolean startOverRequested) {
        this.p1 = p1;
        this.p2 = p2;
        this.startOverRequested = startOverRequested;
    }

    public abstract boolean isSuitable();
    public abstract void blendResult(C constructed);

    public boolean isStartOverRequested() {
        return startOverRequested;
    }
}
