package net.akami.mask.affection;

/**
 * IOCalculationModifier is an implementation of the {@link CalculationAffection} interface. <p>
 * Its behavior is to modify an input (before any calculation) or an output (after the calculation) according to
 * the {@link #modify(String...)} method.
 * <p></p>
 * Usually, the {@link #appliesTo(String...)} check should be performed before going through any modification of the
 * input, although the {@code modify} method might have a defined behavior for non-modifiable inputs (which would
 * basically return the input itself). In this case, the {@code appliesTo} check is not needed, except for possible performances
 * advantages depending on the {@code modify} behavior.
 *
 * @author Antoine Tran
 */
public interface IOCalculationModifier extends CalculationAffection {

    /**
     * Modifies a given input. Should be called assuming that the given input can be modified, or if a
     * non-modifiable input behavior is specified in the body of the method.
     * <p></p>
     * Several IOCalculationModifiers can happen to modify an input at the same time. In this case,
     * the affections with the greater priority level will be called before the others.
     * @param input the given input that needs to be modified
     * @return a modified result from the given input
     */
    String[] modify(String... input);
}
