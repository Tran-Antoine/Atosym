package net.akami.mask.merge;

public abstract class MergeProperty<P> {

    protected P p1;
    protected P p2;
    protected boolean startOverRequested;

    public MergeProperty(P p1, P p2, boolean startOverRequested) {
        this.p1 = p1;
        this.p2 = p2;
        this.startOverRequested = startOverRequested;
    }

    public abstract boolean isSuitable();

    public boolean isStartOverRequested() {
        return startOverRequested;
    }
}
