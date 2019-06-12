package net.akami.mask.merge;

/**
 * A merge producing a result of the same type as the input
 *
 * @see Merge
 * @param <T>
 */
public interface FairMerge<T> extends Merge<T, T> { }
