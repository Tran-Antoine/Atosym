package net.akami.mask.affection;

import java.util.HashMap;
import java.util.Map;

/**
 * CalculationCache is an implementation of the {@link CalculationCanceller} interface that handles
 * a map containing keys under the format 'a|b', mapped with the result of the operation defined by the handler
 * between a and b.
 * <p></p>
 * Therefore, the Adder will call the {@code push(a+'|'+b, a+b} method (pseudo code), whereas the Subtractor will
 * call {@code push(a+'|'+b, a-b)}, and so on. Meaning that each binary operation handler has its own cache.
 * <p></p>
 * Logically, the {@link #resultIfCancelled(String...)} method will return the mapped value corresponding to the
 * key being a|b, and {@link #appliesTo(String...)} will check whether the current calculation has a mapped value or not.
 * <p></p>
 * Depending on the need of the user, the cache might need to have a different capacity, or might even need to be disabled.
 * Hence the {@link #setCapacity(int)} and the {@link #enabled} / {@link #disable()} methods are present, providing
 * more options on cache usage.
 *
 * @author Antoine Tran
 */
public class CalculationCache implements CalculationCanceller {

    private Map<String, String> cache = new HashMap<>();
    private int capacity = 200;
    private boolean enabled;

    @Override
    public String resultIfCancelled(String... input) {
        return cache.get(input[0]+'|'+input[1]);
    }

    @Override
    public boolean appliesTo(String... input) {
        return enabled && cache.containsKey(input[0]+'|'+input[1]);
    }

    /**
     * Maps an input with its result, according to the calculation behavior of the handler.
     * <p>
     * Whenever a calculation is performed, the cache will check whether the calculation has already been
     * calculated earlier on, and will look for the mapped solution if so, to reduce time execution. <p>
     * If the cache is already full (meaning the size of the cache equals the capacity), it will be cleared before
     * adding the given key and value to the map.
     * @param initial the key to add into the cache
     * @param result its mapped value according to the calculation behavior of the handler
     */
    public void push(String initial, String result) {
        if(isFull()) {
            cache.clear();
        }
        cache.put(initial, result);
    }

    /**
     * @return whether the size of the cache equals the capacity
     */
    public boolean isFull() {
        return cache.size() >= capacity;
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public float priorityLevel() {
        return 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
