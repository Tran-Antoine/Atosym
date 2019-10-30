package net.akami.atosym.merge;

import net.akami.atosym.merge.property.SimpleElementMergeProperty;

import java.util.List;

/**
 * Deletes the elements that are found in both lists. Checks for common elements are made by calling
 * {@link #equals(Object)} between both objects.
 * <br><br>
 *
 * This class handles all kinds of objects, since every java object has an {@code equals()} method.
 * <br><br>
 * Note that merging a list with itself won't necessarily return an empty list, because elements with the same index
 * wont be compared. Thus merging {1,2,3,1,3,2} with itself will return an empty list, whereas {1,2,3} won't.
 * @param <T> the kind of object handled by the eraser
 */
public class PairEraser<T> extends SinglePropertySequencedMerge<T> {

    @Override
    public SimpleElementMergeProperty<T> getSingleProperty(T p1, T p2) {
        return new PairNullifyingProperty(p1, p2);
    }

    /**
     * The property used for {@link PairEraser}. Suitable if the two elements are equal (according to the {@link #equals(Object)}
     * method). Simply removes the elements from their former list if suitable. Since
     * both elements are equal, which of the two elements is added to the list should not matter. This object does not provide
     * any control over which element is added.
     */
    public class PairNullifyingProperty extends SimpleElementMergeProperty<T> {

        protected PairNullifyingProperty(T p1, T p2) {
            super(p1, p2, false);
        }

        @Override
        public boolean prepare() {
            return p1.equals(p2);
        }

        @Override
        public void blendResult(List<T> constructed) {
            // nothing to do, both objects have been nullified
        }
    }
}
