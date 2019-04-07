package net.akami.mask.handler;

import net.akami.mask.operation.CalculationCanceller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface CancellableHandler extends AffectionHandler<CalculationCanceller, String> {

    @Override
    default String findResult(String... input) {
        Map<Integer, CalculationCanceller> compatibles = new HashMap<>();
        for(CalculationCanceller affection : getAffections()) {
            if(affection.appliesTo(input))
                compatibles.put(affection.priorityLevel(), affection);
        }
        return compatibles.get(Collections.max(compatibles.keySet())).resultIfCancelled(input);
    }

    default boolean isCancellable(String... input) {
        for(CalculationCanceller affection : getAffections()) {
            if(affection.appliesTo(input))
                return true;
        }
        return false;
    }
}
