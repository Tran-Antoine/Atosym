package net.akami.mask.affection;

import net.akami.mask.handler.IOModificationHandler;

/**
 * The CalculationAffection interface defines the behavior of an expression modifier, in the context of
 * mathematical calculations. Modifier can change the input string given, change the result calculated or even
 * cancel the calculation.
 * Since the different affections might have completely different behaviors, no other
 * methods than {@code appliesTo} and {@code priorityLevel} can be directly implemented from subclasses.
 * <p></p>
 *
 * The two main behavior types provided by the API are the {@link IOCalculationModifier}
 * and the {@link CalculationCanceller} (both inheriting from this interface), which both define what exactly
 * the modification should be, depending on the input string given.
 * <p></p>
 *
 * CalculationAffections must be handled by an {@link net.akami.mask.handler.AffectionHandler} compatible with
 * the type of affection managed, determining when exactly in the process of the calculations a modification must be done.
 * <p></p>
 *
 * Because of the priority level feature, a handler must be easily able to sort its affections from the greater priority
 * to the least. Hence the interface extends {@link Comparable}. If you want to sort a list of affections, do the following :
 * <pre>{@code
 * List<T> compatibles = compatibleAffectionsFor(input);
 * Collections.sort(compatibles);
 * }</pre>
 * @see net.akami.mask.handler.CancellableHandler
 * @see IOModificationHandler
 *
 * @author Antoine Tran
 */
public interface CalculationAffection extends Comparable<CalculationAffection> {

    /**
     * Checks a validity between the input given and the behavior of the current affection.
     * <p></p>
     * The {@code appliesTo} method should be called by the handlers in order to determine if the current
     * affection can be applied to the given input. It avoids unexpected results if the "change nothing" behavior
     * is not implemented by the affection, and it can check whether a different result must be returned, in case the affect is
     * a canceller.
     * @param input the given input string that will be checked
     * @return whether the given input can be modified or if the calculation can be cancelled
     */
    boolean appliesTo(String... input);

    /**
     * Because several affections might want to affect a single input at the same time, the priority level
     * method help the handler deciding which affect will modify the input / cancel the calculation first.
     * <p></p>
     * For instance, the "let fraction or divide" cancelling affect will have a greater priority level than the
     * cache canceller, because even if the result of the operation has already been calculated, it is up to the
     * "let fraction or divide" cancelling effect to determine whether this result is wanted or not.
     * <p></p>
     * A float instead of an integer is used, so that the user is guaranteed to be able to fit as many affections as
     * he wants between others already existing affections. If an integer was being used, only 3 affections could fit
     * between a level 1 priority affection and a level 5 priority affection.
     * <p></p>
     * Note that all affection handlers that are both cancellable and input modifiers should logically check for cancelling first.
     * @return the priority of the affection. Between two different affections, the one with the greatest priority level
     *         will take effect first. If two affections of the same type have the same priority level (which should not happen),
     *         one will be randomly taking effect first.
     */
    float priorityLevel();

    void enable();
    void disable();

    /**
     * Compares the affection itself with another {@link CalculationAffection}. The {@code compareTo} method
     * helps a handler sort its array of affections, so that he gets them from the most priority affection to the least.
     * @param other another affection to compare to the affection itself
     * @return a negative number if the current priority is greater that the other's, 0 if both have the same priority,
     *         and a positive number if the current priority if less than the other's.
     */
    @Override
    default int compareTo(CalculationAffection other) {
        return (int) (other.priorityLevel() - priorityLevel());
    }
}
