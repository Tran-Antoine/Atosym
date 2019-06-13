package net.akami.mask.merge;

import net.akami.mask.merge.property.OverallMergeProperty;

import java.util.List;

public interface Merge<P, R, PROP extends OverallMergeProperty<P, R>> {

    R merge(P p1, P p2, boolean selfMerge);
    List<PROP> generateOverallProperties(P p1, P p2);
}
