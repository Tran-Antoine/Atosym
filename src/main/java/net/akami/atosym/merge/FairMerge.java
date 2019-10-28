package net.akami.atosym.merge;

/**
 * A merge producing a result of the same type as the input
 *
 * @see Merge
 * @param <T> the type of input and output handled by the merge behavior
 */
public interface FairMerge<T> extends Merge<T, T> { }
