package net.akami.mask.alteration;

/**
 * A Canceller checks, as every alteration, if it can have an impact on a certain expression. If yes, then it cancels
 * the scheduled calculation, and gives a result that might be different or not than if the calculation was computed.
 * <br>
 * For instance, the {@link CalculationCache} checks whether the expression has already been evaluated, and gives
 * the result stored if it is the case.
 */
public interface CalculationCanceller<T> extends CalculationAlteration<T> {

    /**
     * Defines what merge must be returned instead of the initial calculation.
     * <br>
     * Note that no validity check must be done inside the method itself, since the {@link #appliesTo(Object...)}
     * method should already take care of that.
     * @param input the given input. Depending on the calculation, the getElementsSize of the array might change.
     * @return a merge that might be different or not than the merge that would have been computed without the
     * intervention of the canceller.
     */
    T resultIfCancelled(T... input);
}
