package net.akami.mask.affection;

import java.util.HashMap;
import java.util.Map;

public class CalculationCache implements CalculationCanceller {

    private Map<String, String> cache = new HashMap<>();
    private int capacity = 200;

    @Override
    public String resultIfCancelled(String... input) {
        return cache.get(input[0]+'|'+input[1]);
    }

    @Override
    public boolean appliesTo(String... input) {
        return cache.containsKey(input[0]+'|'+input[1]);
    }

    public void push(String initial, String result) {
        if(cache.size() >= capacity) {
            cache.clear();
        }
        cache.put(initial, result);
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
