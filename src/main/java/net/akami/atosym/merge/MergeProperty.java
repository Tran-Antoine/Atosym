package net.akami.atosym.merge;

/**
 * Property for element merging. Every property is generated for each pair of elements.
 * @param <P> the type of element handled by the property
 */
public abstract class MergeProperty<P> {

    protected P p1;
    protected P p2;

    public MergeProperty(P p1, P p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public abstract boolean prepare();
}
