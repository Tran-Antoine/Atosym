package net.akami.mask.merge;

import java.util.Set;

/**
 *
 * WARNING : The javadoc of this interface is not up to date thus is not valid. It will be rewritten soon.
 *
 *
 * Describes an element which, once in a list, can be merged with another element from another list or from
 * the list itself, under certain conditions.
 * Basically, you have two lists, both containing the same kind of elements. Theses elements must
 * implement the MergeBehavior interface. You can then merge these two lists into one single list. if two elements from
 * different lists are mergeables (defined by the {@link #isMergeable(T, T)} method), a merge defined by the
 * {@link #mergeElement(T, T)} method will be added into a new list. In the end, all elements which could not be
 * merged will be added into the new list.
 *
 * Example :
 *
 * Our object SuperNumber implements the MergeBehavior interface. {@code isMergeable()} returns true if the float handled
 * by the SuperNumber class equals the float of the compared SuperNumber.
 *
 * <pre>
 * {@code
 * @Override
 * public boolean isMergeable(SuperNumber other) {
 *     return this.value == other.value;
 * }
 * }
 * </pre>
 *
 * If this is the case, the computed element will be the sum of the two floats, being one of them multiplied by two.
 * <pre>
 * {@code
 * @Override
 * public SuperNumber mergeElement(SuperNumber other) {
 *     return new SuperNumber(this.number * 2);
 * }
 * }
 * </pre>
 *
 * Let's now create two lists :
 * <pre>
 * {@code
 * List<SuperNumber> l1 = Arrays.asList(new SuperNumber(1), new SuperNumber(3), new SuperNumber(10));
 * List<SuperNumber> l2 = Arrays.asList(new SuperNumber(0), new SuperNumber(2), new SuperNumber(10));
 *
 * List<SuperNumber> merge = MergeBehavior.merge(l1, l2);
 * }</pre>
 * The {@code merge} list contains the super numbers {@code 0, 1, 2, 3, and 20}.
 * @param <T>
 */
public interface MergeBehavior<T> {

    /**
     * Defines whether an element can be effectively merged with another.
     * If yes, then {@link #mergeElement(MergeBehavior)} will be called, computing a new merge from
     * the two elements.
     * <pre></pre>
     * Note that the interface automatically refuses any merge including two elements from different types.
     * Thus no instance check or cast is required inside the method.
     *
     * @param other the other element to compare with the object itself
     * @return whether the two elements are mergeable
     */
    boolean isMergeable(T a, T b);

    /**
     * Computes a defined merge from two elements, which will be added instead of them in the new list.
     * The {@code mergeElement} method will only be called if the two elements are compatible, and can
     * successfully create a new merge.
     * @param other
     * @return a merge computed from the object itself and the parameter
     */
    T mergeElement(T a, T b);

    Class<? extends T> getHandledType();

}
