package net.akami.atosym.merge;

import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.merge.property.OverallMergeProperty;

/**
 * A merge producing a result of the same type as the input
 *
 * @see Merge
 * @param <T> the type of input and output handled by the merge behavior
 * @param <PROP> the type of {@link OverallMergeProperty} handled
 */
public interface FairMerge<T, PROP extends FairOverallMergeProperty<T>> extends Merge<T, T, PROP> { }
