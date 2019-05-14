package net.akami.mask.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Describes an element which, once in a list, can be merged with another element from another list or from
 * the list itself, under certain conditions.
 * Basically, you have two lists, both containing the same kind of elements. Theses elements must
 * implement the Mergeable interface. You can then merge these two lists into one single list. if two elements from
 * different lists are mergeables (defined by the {@link #isMergeable(T)} method), a result defined by the
 * {@link #mergeElement(T)} method will be added into a new list. In the end, all elements which could not be
 * merged will be added into the new list.
 *
 * Example :
 *
 * Our object SuperNumber implements the Mergeable interface. {@code isMergeable()} returns true if the float handled
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
 * List<SuperNumber> result = Mergeable.merge(l1, l2);
 * }</pre>
 * The {@code result} list contains the super numbers {@code 0, 1, 2, 3, and 20}.
 * @param <T>
 */
public interface Mergeable<T extends Mergeable> {

    /**
     * Defines whether an element can be effectively merged with another.
     * If yes, then {@link #mergeElement(Mergeable)} will be called, computing a new result from
     * the two elements.
     * <pre></pre>
     * Note that the interface automatically refuses any merge including two elements from different types.
     * Thus no instance check or cast is required inside the method.
     *
     * @param other the other element to compare with the object itself
     * @return whether the two elements are mergeable
     */
    boolean isMergeable(T other);

    /**
     * Computes a defined result from two elements, which will be added instead of them in the new list.
     * The {@code mergeElement} method will only be called if the two elements are compatible, and can
     * successfully create a new result.
     * @param other
     * @return a result computed from the object itself and the parameter
     */
    T mergeElement(T other);

    /**
     * Merges a list with itself. Basically, Calls the {@code merge} method with the parameter twice.
     * See {@link #secureMerge(List, List)} for further information.
     *
     * @param self the list itself
     * @param <T> the type of the list
     * @return {@code secureMerge(self, self}
     */
    static <T extends Mergeable> List<T> secureMerge(List<T> self) {
        return merge(new ArrayList<>(self));
    }
    /**
     * Merges the lists given without modifying them.
     * The method basically creates a copy of the two lists, then returns {@link #merge(List, List)}.
     * @param l1 the first list to merge
     * @param l2 the second list to merge
     * @param <T> the kind of mergeable item given
     * @return {@link #merge(List, List)}
     */
    static <T extends Mergeable> List<T> secureMerge(List<T> l1, List<T> l2) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2));
    }

    /**
     * Merges a list with itself. The initial list will most likely be changed. If you don't want
     * the list to be changed, use {@link #secureMerge(List)} instead.
     * @param self the list to merge with itself.
     * @param <T> the type of the list
     * @return {@code merge(self, self)}
     */
    static <T extends Mergeable> List<T> merge(List<T> self) {
        return merge(self, self, true);
    }

    /**
     * Merges two different lists. Every element that can be merged, according to the {@link #isMergeable(Mergeable)}
     * method will be set to null in the initial list they belong to. Otherwise, they will remain in their initial
     * list, but also in the new created one. If you don't want the initial lists to be changed, use
     * {@link #secureMerge(List, List)} instead.
     * <pre></pre>
     * Note that if you put the same list twice in the parameters, they will be considered as two different lists,
     * thus every element might be merged with itself. Use {@link #merge(List)} if you don't want the elements
     * to merge with themselves.
     * @param l1 the first list to merge
     * @param l2 the second list to merge
     * @param <T> the type of the two lists
     * @return a new list containing the merged value of the parameters, plus the values that could not be merged
     * successfully
     */
    static <T extends Mergeable> List<T> merge(List<T> l1, List<T> l2) {
        return merge(l1, l2, false);
    }

    /**
     * Use {@link #merge(List)} or {@link #merge(List, List)} instead. Private interface methods are not
     * available in Java 8.
     */
    static  <T extends Mergeable> List<T> merge(List<T> l1, List<T> l2, boolean singleList) {
        if(l1 == null) return l2;
        if(l2 == null) return l1;
        List<T> finalResult = new ArrayList<>();
        int i = 0;
        for(T element : l1) {
            if(element == null) {i++; continue;}
            int j = 0;
            for(T element2 : l2) {
                if(singleList && i == j) {j++; continue;}
                if(element2 == null) {j++;continue;}
                // Prevents potential errors from the next two lines (unchecked call + unchecked cast)
                if(!element.getClass().equals(element2.getClass())) {j++; continue;}

                if(element.isMergeable(element2)) {
                    T localResult = (T) element.mergeElement(element2);
                    l1.set(i, null);
                    l2.set(j, null);

                    finalResult.add(localResult);
                    break;
                }
                j++;
            }
            i++;
        }
        finalResult.addAll(l1.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        if(!singleList)
            finalResult.addAll(l2.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return finalResult;
    }
}
