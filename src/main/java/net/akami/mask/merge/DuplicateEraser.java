package net.akami.mask.merge;

import net.akami.mask.merge.property.ElementSequencedMergeProperty;

import java.util.List;

/**
 * Similar to {@link PairEraser}, although instead of nullifying common elements, it just adds one of them in the constructed list. <p>
 * Equality checks are performed through the {@link #equals(Object)} method. Whenever two elements are equal, they
 * are removed from their former list, and one of them is added to the new list.
 * @param <T> the kind of object handled by the duplicate eraser
 */
public class DuplicateEraser<T> implements SinglePropertySequencedMerge<T> {

    @Override
    public ElementSequencedMergeProperty<T> getSingleProperty(T p1, T p2) {
        return new DuplicateEraserProperty(p1, p2);
    }

    /**
     * The property used for {@link PairEraser}. Suitable if the two elements are equal (according to the {@link #equals(Object)}
     * method). Simply removes the elements from their former list if suitable, then adds one of them in the new list. Since
     * both elements are equal, which of the two elements is added to the list should not matter. This object does not provide
     * any control over which element is added.
     */
    public class DuplicateEraserProperty extends ElementSequencedMergeProperty<T> {

        protected DuplicateEraserProperty(T p1, T p2) {
            super(p1, p2, false);
        }

        @Override
        public boolean isSuitable() {
            return p1.equals(p2);
        }

        @Override
        public void blendResult(List<T> constructed) {
            constructed.add(p1);
        }
    }
}
