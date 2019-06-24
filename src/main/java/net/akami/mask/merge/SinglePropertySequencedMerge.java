package net.akami.mask.merge;

import net.akami.mask.merge.property.ElementSequencedMergeProperty;

import java.util.Collections;
import java.util.List;

/**
 * A simple wrapper for {@link SequencedMerge}s that only have one {@link ElementSequencedMergeProperty}. <br>
 * Since a lot of sequenced merge seem to only require one property, this interfaces serves as a shortcut.
 */
public interface SinglePropertySequencedMerge<T> extends SequencedMerge<T> {

    /**
     * Creates a single property from the given input. See {@link #generateElementProperties(Object, Object)}
     * for further information
     * @param p1 the element from the first list
     * @param p2 the element from the second list
     * @return a single property suiting both elements
     */
    ElementSequencedMergeProperty<T> getSingleProperty(T p1, T p2);

    @Override
    default List<ElementSequencedMergeProperty<T>> generateElementProperties(T p1, T p2) {
        return Collections.singletonList(getSingleProperty(p1, p2));
    }
}
