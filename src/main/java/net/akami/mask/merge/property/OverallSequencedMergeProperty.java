package net.akami.mask.merge.property;

import net.akami.mask.merge.SequencedMergeProperty;

import java.util.List;

/**
 * Properties which have an appliance depending on both entire sequences. If a property is detected as
 * suitable for the two lists, it will be able to compute a full result from the two lists, and potentially require
 * a restart merge.
 * @param <T>
 */
public abstract class OverallSequencedMergeProperty<T> extends SequencedMergeProperty<List<T>, List<T>> {

    public OverallSequencedMergeProperty(List<T> p1, List<T> p2, boolean startOverRequested) {
        super(p1, p2, startOverRequested);
    }
}
