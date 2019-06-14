package net.akami.mask.merge.property;

import net.akami.mask.merge.MergeProperty;

/**
 * Properties which have an appliance depending on both entire sequences. If a property is detected as
 * suitable for the two lists, it will be able to compute a full result from the two lists, and potentially require
 * a restart merge.
 * @param <P>
 */
public abstract class OverallMergeProperty<P, R> extends MergeProperty<P> {

    public OverallMergeProperty(P p1, P p2, boolean startOverRequested) {
        super(p1, p2, startOverRequested);
    }

    public abstract R computeResult();
}
