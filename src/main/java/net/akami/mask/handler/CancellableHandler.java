package net.akami.mask.handler;

import net.akami.mask.affection.CalculationCanceller;

import java.util.*;

public interface CancellableHandler<T> extends AlterationHandler<CalculationCanceller<T>, T> {

    @Override
    default T findResult(T... input) {
        List<CalculationCanceller<T>> compatibles = compatibleAlterationsFor(input);
        Collections.sort(compatibles);

        return compatibles.get(0).resultIfCancelled(input);
    }

    default boolean isCancellable(T... input) {
        for(CalculationCanceller<T> affection : getAffections()) {
            if(affection.appliesTo(input))
                return true;
        }
        return false;
    }
}
