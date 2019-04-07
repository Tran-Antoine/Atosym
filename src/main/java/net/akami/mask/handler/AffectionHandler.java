package net.akami.mask.handler;

import net.akami.mask.operation.CalculationAffection;

import java.util.Optional;

public interface AffectionHandler<T extends CalculationAffection, R> {

    T[] getAffections();

    default <S extends T> Optional<S> getAffection(Class<S> type) {
        for(T affection : getAffections()) {
            if(affection.getClass().equals(type))
                return (Optional<S>) Optional.of(affection);
        }
        return Optional.empty();
    }

    R findResult(String... input);
}
