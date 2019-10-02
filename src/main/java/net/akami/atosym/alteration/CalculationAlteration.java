package net.akami.atosym.alteration;

import net.akami.atosym.handler.AlterationHandler;

import java.util.List;

/**
 * Defines the behavior of an expression modifier, in the context of
 * mathematical calculations. Modifiers can change the input string given, change the result calculated or even
 * cancel the calculation.
 * Since the different alterations might have completely different behaviors, no other
 * methods than {@code appliesTo} and {@code priorityLevel} can be directly implemented from subclasses.
 * <br>
 *
 * The two main behavior types provided by the API are the {@link IOCalculationModifier}
 * and the {@link CalculationCanceller} (both inheriting from this interface), which both define what exactly
 * the modification should be, depending of the input string given.
 * <br>
 *
 * Calculation alterations must be handled by an {@link AlterationHandler} compatible with
 * the type of alteration managed, determining when exactly in the process of the calculations a modification must be done.
 * <br>
 *
 * Because of the priority level feature, a handler must be easily able to sort its alterations from the greater priority
 * to the least. Hence the interface extends {@link Comparable}. If you want to sort a list of alterations, do the following :
 * <pre>{@code
 * List<T> compatibles = someAlterationHandler.compatibleAlterationsFor(input);
 * Collections.sort(compatibles);
 * }</pre>
 * @see AlterationHandler
 *
 * @author Antoine Tran
 */
public interface CalculationAlteration<T> extends Comparable<CalculationAlteration> {

    /**
     * Checks a validity between the input given and the behavior of the current alteration.
     * <br>
     * The {@code appliesTo} method should be called by the handlers in order to determine if the current
     * alteration can be applied to the given input. It avoids unexpected results if the "change nothing" behavior
     * is not implemented by the alteration, and it can check whether a different merge must be returned, in case the affect is
     * a canceller.
     * @param input the given input string that will be checked
     * @return whether the given input can be modified or if the calculation can be cancelled
     */
    boolean appliesTo(List<T> input);

    /**
     * Because several alterations might want to affect a single input at the same time, the priority level
     * method helps the handler deciding which affect will modify the input / cancel the calculation first.
     * <br>
     * For instance, the "let fraction or divide" cancelling affect will have a greater priority level than the
     * cache canceller, because even if the merge of the operation has already been calculated, it is up to the
     * "let fraction or divide" cancelling effect to determine whether this merge is wanted or not.
     * <br>
     * A float instead of an integer is used, so that the user is guaranteed to be able to fit as many alterations as
     * he wants between others already existing alterations. If an integer was being used, only 3 alterations could fit
     * between a level 1 priority alteration and a level 5 priority alteration.
     * <br>
     * Note that all alterations that are both cancellable and input modifiers should logically check for cancelling first.
     * @return the priority of the alteration. Between two different alterations, the one with the greatest priority level
     *         will take effect first. If two alterations of the same type have the same priority level (which should not happen),
     *         one will be randomly taking effect first.
     */
    float priorityLevel();

    /**
     * Compares the alteration itself with another {@link CalculationAlteration}. The {@code compareTo} method
     * helps a handler sort its array of alterations, so that he gets them from the most priority alteration to the least.
     * @param other another alteration to compare to the alteration itself
     * @return a negative number if the current priority is greater that the other's, 0 if both have the same priority,
     *         and a positive number if the current priority if less than the other's.
     */
    @Override
    default int compareTo(CalculationAlteration other) {
        float r = other.priorityLevel() - priorityLevel();
        return r < 0 ? -1 : r > 0 ? 1 : 0;
    }
}
