package net.akami.mask.handler;

import net.akami.mask.alteration.CalculationAlteration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents objects which handle
 * @param <T>
 * @param <R>
 * @param <I>
 */
public interface AlterationHandler<T extends CalculationAlteration, R, I> {

    List<T> getAffections();

    default <S extends T> Optional<S> getAffection(Class<S> type) {
        for(T affection : getAffections()) {
            if(affection.getClass().equals(type))
                return (Optional<S>) Optional.of(affection);
        }
        return Optional.empty();
    }

    default List<T> compatibleAlterationsFor(R... input) {
        List<T> compatibles = new ArrayList<>();

        for(T affection : getAffections()) {
            if(affection.appliesTo(input))
                compatibles.add(affection);
        }

        return compatibles;
    }

    R findResult(I input);
}
