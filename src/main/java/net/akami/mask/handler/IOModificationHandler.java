package net.akami.mask.handler;

import net.akami.mask.alteration.IOCalculationModifier;

import java.util.Collections;
import java.util.List;

public interface IOModificationHandler<T> extends AlterationHandler<IOCalculationModifier<T>, T[], T[]> {

    @Override
    default T[] findResult(T... input) {
        List<IOCalculationModifier<T>> affections = compatibleAlterationsFor(input);
        Collections.sort(affections);
        for(IOCalculationModifier<T> affection : affections) {
            input = affection.modify(input);
        }

        return input;
    }
}
