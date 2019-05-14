package net.akami.mask.affection;

/**
 * The CalculationCanceller class is one of the two core object of the affection system, provided by the API.
 * A Canceller checks, as every affection, if it can have an impact on a certain expression. If yes, then it cancels
 * the scheduled calculation, and gives a result that might be different or not than if the calculation was computed.
 * <p></p>
 * For instance, the {@link CalculationCache} checks whether the expression has already been evaluated, and gives
 * the result stored if it is the case.
 */
public interface CalculationCanceller<T> extends CalculationAffection<T> {

    /**
     * Defines what result must be returned instead of the initial calculation.
     * <p></p>
     * Note that no validity check must be done inside the method itself, since the {@link #appliesTo(String...)}
     * method should already take care of that.
     * @param input the given input. Depending on the calculation, the length of the array might change.
     * @return a result that might be different or not than the result that would have been computed without the
     * intervention of the canceller.
     */
    T resultIfCancelled(T... input);
}
