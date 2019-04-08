package net.akami.mask.handler;

import net.akami.mask.affection.CalculationCanceller;

import java.util.*;

public interface CancellableHandler extends AffectionHandler<CalculationCanceller, String> {

    @Override
    default String findResult(String... input) {
        List<CalculationCanceller> compatibles = compatibleAffectionsFor(input);
        Collections.sort(compatibles);

        return compatibles.get(0).resultIfCancelled(input);
    }

    default boolean isCancellable(String... input) {
        for(CalculationCanceller affection : getAffections()) {
            if(affection.appliesTo(input))
                return true;
        }
        return false;
    }
}
