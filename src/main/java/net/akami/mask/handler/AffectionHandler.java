package net.akami.mask.handler;

import net.akami.mask.affection.CalculationAffection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * AffectionHandler is the base class that supports {@link CalculationAffection}s' management. All classes needing to
 * perform actions from CalculationAffections must implement directly (or, most of the time, indirectly) this interface.
 * It provides a {@link #getAffections()} abstract method, which defines what affections are to be handled by the object,
 * as well as some util methods, such as {@link #getAffection(Class)} and {@link #compatibleAffectionsFor(String...)}.
 * <p></p>
 * The {@link #findResult(String...)} method describes what result should be returned according to all the different
 * affections. See {@link CalculationAffection#priorityLevel()} for further information.
 * <p></p>
 * Two implementation of this interface are provided by the Mask api :
 *
 * <ul>
 * <li> {@link CancellableHandler} allows the handler to cancel calculations if at least one of the affections
 * has the {@code appliesTo} returning true with the given input
 * <li> {@link IOModificationHandler} allows the handler to modify either the input before computing any result (pre-formatting),
 * or the output computed (post-formatting).
 * </li>
 * </ul>
 *
 * Each of these implementations redefine the {@code findResult} method, so that classes which implement them don't
 * need to take care of it.
 *
 * @param <T> the generic type of supported affections
 * @param <R> the generic type of the result returned by the handler after modifying / cancelling an expression
 *
 * @author Antoine Tran
 */
public interface AffectionHandler<T extends CalculationAffection, R> {

    T[] getAffections();

    default <S extends T> Optional<S> getAffection(Class<S> type) {
        for(T affection : getAffections()) {
            if(affection.getClass().equals(type))
                return (Optional<S>) Optional.of(affection);
        }
        return Optional.empty();
    }

    default List<T> compatibleAffectionsFor(String... input) {
        List<T> compatibles = new ArrayList<>();

        for(T affection : getAffections()) {
            if(affection.appliesTo(input))
                compatibles.add(affection);
        }

        return compatibles;
    }

    R findResult(String... input);
}
