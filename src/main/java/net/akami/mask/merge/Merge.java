package net.akami.mask.merge;

public interface Merge<T, R> {

    R merge(T p2, T p1, boolean selfMerge);
}
