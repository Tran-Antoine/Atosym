package net.akami.atosym.merge;

import net.akami.atosym.merge.property.FairElementMergeProperty;

import java.util.Collections;
import java.util.List;

/**
 * A simple wrapper for {@link FairSequencedMerge}s that only have one {@link FairElementMergeProperty}. <br>
 * Since a lot of sequenced merge seem to only require one property, this interfaces serves as a shortcut.
 */
public abstract class SinglePropertySequencedMerge<T> extends FairSequencedMerge<T> {

    /**
     * Creates a single property from the given input. See {@link #loadPropertiesFrom(T, T)}
     * for further information
     * @param p1 the element from the first list
     * @param p2 the element from the second list
     * @return a single property suiting both elements
     */
    protected abstract FairElementMergeProperty<T> getSingleProperty(T p1, T p2);

    @Override
    public List<FairElementMergeProperty<T>> loadPropertiesFrom(T p1, T p2) {
        return Collections.singletonList(getSingleProperty(p1, p2));
    }
}
