package net.akami.mask.merge;

import net.akami.mask.merge.property.ElementSequencedMergeProperty;

import java.util.List;

/**
 * Deletes the elements that are found in both lists. Checks for common elements are made by calling
 * {@link #equals(Object)} between both objects.
 * <pre></pre>
 *
 * This class handles all kinds of objects, since every java object has an {@code equals()} method.
 * <pre></pre>
 * Note that merging a list with itself won't necessarily return an empty list, because elements with the same index
 * wont be compared. Thus merging {1,2,3,3,2,1} with itself will return an empty list, whereas {1,2,3} won't.
 */
public class PairEraser<T> implements SinglePropertySequencedMerge<T> {

    @Override
    public ElementSequencedMergeProperty<T> getSingleProperty(T p1, T p2) {
        return new PairNullifyingProperty(p1, p2);
    }

    public class PairNullifyingProperty extends ElementSequencedMergeProperty<T> {

        protected PairNullifyingProperty(T p1, T p2) {
            super(p1, p2, false);
        }

        @Override
        public boolean isSuitable() {
            return p1.equals(p2);
        }

        @Override
        public void blendResult(List<T> constructed) {
            // nothing to do, both objects have been nullified
        }
    }
}
