package net.akami.atosym.alteration;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.operator.BinaryOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A {@link CalculationCanceller} added by default in the library that avoids calculation repetitions.
 * <br>
 * Depending on the need of the user, it might be useful or completely useless. The class stores expressions previously calculated at
 * runtime, to reduce the execution time if one single calculation is performed several times.
 * <br>
 * All binary handlers contain a calculation cache by default. If you want to disable the cache, as for disabling
 * every modifier, use {@link MaskContext#removeGlobalCanceller(FairCalculationCanceller, Class)}. <br>
 * The reason why the cache might need to be disabled is that memory access to the HashMap can possibly be slower than
 * certain calculations. If you are guaranteed that only small calculations will be performed, then disable the cache.
 *
 * @author Antoine Tran
 */
public class CalculationCache implements FairCalculationCanceller<MathObject> {

    private Map<String, MathObject> cache = new HashMap<>();
    private int capacity;

    /**
     * Constructs a cache with a 200 slots capacity
     */
    public CalculationCache() {
        this(200);
    }

    /**
     * Constructs a cache with the given capacity
     * @param capacity the given capacity
     */
    public CalculationCache(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public MathObject resultIfCancelled(List<MathObject> input) {
        return cache.get(input.get(0).toString()+'|'+input.get(1).toString());
    }

    @Override
    public boolean appliesTo(List<MathObject> input) {
        if (capacity == 0) return false;
        if (input == null) return false;
        if (input.size() != 2) return false;

        return cache.containsKey(input.get(0).toString()+'|'+input.get(1).toString());
    }

    /**
     * Adds a new merge into the HashMap. Data should always be pushed using the same format, otherwise the key for a given
     * input might not be detected, even though the calculation was already performed.
     * If the cache is full, its size being specified with the {@link #setCapacity(int)} method, the map will be cleared
     * before entering any new data.
     * <br>
     * Regular {@link BinaryOperator}s use the following format :
     * <br>
     *
     * <pre>
     * Initial : a|b
     * Result  : computed merge of a "something" b
     *
     * -| Example |-
     * Initial : 3|2
     * Result  : 5
     * </pre>
     * Because each handler has its own cache, there is no need to specify which operation sign a and b are linked by.
     *
     * @param initial the input, computing a merge with a given operation
     * @param result the merge of the computed version of the input
     */
    public void push(String initial, MathObject result) {
        if(capacity == 0) return;

        if(cache.size() >= capacity) {
            cache.clear();
        }
        cache.put(initial, result);
    }

    @Override
    public float priorityLevel() {
        return 0;
    }

    /**
     * @return the capacity of the cache
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity a new capacity for the cache
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public static Supplier<FairCalculationCanceller<MathObject>> supply(int capacity) {
        return () -> new CalculationCache(capacity);
    }
}
