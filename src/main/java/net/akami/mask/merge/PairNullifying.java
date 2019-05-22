package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;

/**
 * Deletes the elements that are found in both lists. To define whether an element from the first list
 * is also present in the second one, the {@link #equals(Object)} method is called.
 * <pre></pre>
 *
 * It handles all kinds of objects, since every java object has an {@code equals()} method.
 * <pre></pre>
 * Note that merging a list with itself won't necessarily return an empty list, because elements with the same index
 * wont be compared. Thus merging {1,2,3,3,2,1} with itself will return an empty list, whereas {1,2,3} won't.
 */
public class PairNullifying implements MergeBehavior<Object> {

    private MaskContext context;

    public PairNullifying(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isMergeable(Object a, Object b) {
        return a.equals(b);
    }

    @Override
    public Object mergeElement(Object a, Object b) {
        return null;
    }

    @Override
    public Class<?> getHandledType() {
        return Object.class;
    }
}
