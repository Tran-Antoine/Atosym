package net.akami.atosym.merge.property;

public abstract class FairOverallMergeProperty<P> extends OverallMergeProperty<P, P> {

    public FairOverallMergeProperty(P p1, P p2, boolean startOverRequested) {
        super(p1, p2, startOverRequested);
    }
}
