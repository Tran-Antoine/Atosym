package net.akami.mask.merge;

import net.akami.mask.merge.property.ElementSequencedMergeProperty;

import java.util.List;

public class DuplicateEraser<T> implements SinglePropertySequencedMerge<T> {

    @Override
    public ElementSequencedMergeProperty<T> getSingleProperty(T p1, T p2) {
        return new DuplicateEraserProperty(p1, p2);
    }

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
