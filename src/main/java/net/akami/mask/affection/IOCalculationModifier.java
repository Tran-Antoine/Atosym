package net.akami.mask.affection;

import net.akami.mask.handler.IODefaultFormatter;

/**
 * The IOCalculationModifier class is one of the two core object of the affection system, provided by the API.
 * An IOModifier checks whether a given expression needs to be formatted or not. If yes, it replaces the input by its
 * formatted version. The modifications can be applied before or after performing a calculation.
 * <p></p>
 * For instance, the angle unit modifier will check before performing a trigonometric operation whether it must convert
 * the input to radians before proceeding.
 * On the other hand, the fraction modifier will check after performing any operation whether the result must be
 * transformed to a fraction or not.
 * <p></p>
 * Note that IOCalculationModifiers are different than the {@link IODefaultFormatter}s.
 * Basically, the default formatters cannot be enabled / disabled, nor configured. They exist for handlers requiring
 * expressions under a certain format to work. They thus aren't part of the affection system.
 */
public interface IOCalculationModifier extends CalculationAffection {

    /**
     * Defines how a given input must be formatted.
     * <p></p>
     * Note that no validity check must be done inside the method itself, since the {@link #appliesTo(String...)}
     * method should already take care of that.
     * @param input the given input
     * @return the formatted version of the input
     */
    String[] modify(String... input);
}
