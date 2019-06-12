package net.akami.mask.merge;

import net.akami.mask.merge.property.ElementSequencedMergeProperty;

import java.util.List;

public interface SinglePropertySequencedMerge<T> extends SequencedMerge<T> {

    ElementSequencedMergeProperty<T> getSingleProperty(T p1, T p2);

    @SuppressWarnings("unchecked")
    @Override
    default List<ElementSequencedMergeProperty<T>> generateElementProperties(T p1, T p2) {
        return new ElementSequencedMergeProperty[]{getSingleProperty(p1, p2)};
    }
}
