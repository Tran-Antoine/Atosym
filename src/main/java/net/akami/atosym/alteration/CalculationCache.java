package net.akami.atosym.alteration;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.Expression;

import java.util.HashMap;
import java.util.Map;

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
public class CalculationCache implements FairCalculationCanceller<Expression> {

    private Map<String, Expression> cache = new HashMap<>();
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
    public Expression resultIfCancelled(Expression... input) {
        return cache.get(input[0].toString()+'|'+input[1].toString());
    }

    @Override
    public boolean appliesTo(Expression... input) {
        if (capacity == 0) return false;
        return cache.containsKey(input[0].toString()+'|'+input[1].toString());
    }

    /**
     * Adds a new merge into the HashMap. Data should always be pushed using the same format, otherwise the key for a given
     * input might not be detected, even though the calculation was already performed.
     * If the cache is full, its size being specified with the {@link #setCapacity(int)} method, the map will be cleared
     * before entering any new data.
     * <br>
     * Regular {@link net.akami.atosym.handler.BinaryOperationHandler}s use the following format :
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
    public void push(String initial, Expression result) {
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
}
