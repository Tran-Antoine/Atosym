package net.akami.mask.handler;

import net.akami.mask.alteration.CalculationCanceller;
import net.akami.mask.alteration.IOCalculationModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents objects which handle
 */
public interface AlterationHandler<T, R> {

    List<CalculationCanceller<T>> getCancellers();
    List<IOCalculationModifier<T>> getModifiers();

    default Optional<CalculationCanceller<T>> getSuitableCanceller(T... input) {
        for(CalculationCanceller<T> affection : getCancellers()) {
            if(affection.appliesTo(input)) return Optional.of(affection);
        }
        return Optional.empty();
    }

    default List<IOCalculationModifier<T>> getSuitableModifiers(T... input) {
        List<IOCalculationModifier<T>> compatibles = new ArrayList<>();

        for(IOCalculationModifier<T> affection : getModifiers()) {
            if(affection.appliesTo(input)) compatibles.add(affection);
        }
        Collections.sort(compatibles);
        return compatibles;
    }

    default <S extends CalculationCanceller<T>> Optional<S> getCanceller(Class<S> clazz) {
        for(CalculationCanceller<T> canceller : getCancellers()) {
            // cast is secured
            if(canceller.getClass().equals(clazz)) return (Optional<S>) Optional.of(canceller);
        }
        return Optional.empty();
    }

    default <S extends IOCalculationModifier<T>> Optional<S> getModifier(Class<S> clazz) {
        for(IOCalculationModifier<T> canceller : getModifiers()) {
            // cast is secured
            if(canceller.getClass().equals(clazz)) return (Optional<S>) Optional.of(canceller);
        }
        return Optional.empty();
    }
}
