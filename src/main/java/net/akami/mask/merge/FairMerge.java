package net.akami.mask.merge;

import net.akami.mask.merge.property.FairOverallMergeProperty;

/**
 * A merge producing a result of the same type as the input
 *
 * @see Merge
 * @param <T>
 */
public interface FairMerge<T, PROP extends FairOverallMergeProperty<T>> extends Merge<T, T, PROP> { }
