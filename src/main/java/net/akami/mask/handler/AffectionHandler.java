package net.akami.mask.handler;

import net.akami.mask.affection.CalculationAffection;

import java.util.ArrayList;
import java.util.List;
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

    default List<T> compatibleAffectionsFor(R... input) {
        List<T> compatibles = new ArrayList<>();

        for(T affection : getAffections()) {
            if(affection.appliesTo(input))
                compatibles.add(affection);
        }

        return compatibles;
    }

    R findResult(R... input);
}
