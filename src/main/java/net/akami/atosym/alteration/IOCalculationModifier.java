package net.akami.atosym.alteration;

/**
 * An IOModifier checks whether a given expression needs to be formatted or not. <br>
 * When required, it replaces the input by its formatted version. The modifications can be applied before
 * or after performing a calculation. <br>
 * For instance, the angle unit modifier will check before performing a trigonometric operation whether it must convert
 * the input to radians before proceeding.
 * On the other hand, the fraction modifier will check after performing any operation whether the merge must be
 * transformed to a fraction or not.
 */
public interface IOCalculationModifier<T> extends CalculationAlteration<T> {

    /**
     * Defines how a given input must be formatted.
     * <br>
     * Note that no validity check must be done inside the method itself, since the {@link #appliesTo(Object[])}
     * method should already take care of that.
     * @param input the given input
     * @return the formatted version of the input
     */
    T[] modify(T... input);
}
