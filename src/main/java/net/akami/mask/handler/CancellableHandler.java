package net.akami.mask.handler;

import net.akami.mask.operation.CalculationAffection;
import net.akami.mask.operation.CalculationCanceller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface CancellableHandler extends AffectionHandler {

    CalculationCanceller[] getCancellers();

    default String result(String... input) {
        Map<Integer, CalculationCanceller> compatibles = new HashMap<>();
        for(CalculationCanceller affection : getCancellers()) {
            if(affection.appliesTo(input))
                compatibles.put(affection.priorityLevel(), affection);
        }
        return compatibles.get(Collections.max(compatibles.keySet())).resultIfCancelled(input);
    }

    default  <T extends CalculationAffection> Optional<T> getAffection(Class<T> type) {
        for(CalculationAffection affection : getCancellers()) {
            if(affection.getClass().equals(type))
                return (Optional<T>) Optional.of(affection);
        }
        return Optional.empty();
    }

    default boolean isCancellable(String... input) {
        for(CalculationCanceller affection : getCancellers()) {
            if(affection.appliesTo(input))
                return true;
        }
        return false;
    }
}
