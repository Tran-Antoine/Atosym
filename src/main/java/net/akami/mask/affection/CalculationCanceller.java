package net.akami.mask.affection;

/**
 * CalculationCanceller is an implementation of the {@link CalculationAffection} interface. <p>
 * Its behavior should be to check at the right time whether the affection can be applied to a given input,
 * and if so, to return a result from this input.
 * <p></p>
 * The defined {@link #resultIfCancelled(String...)} converts the input to the wanted result.
 * <p></p>
 * Obviously, this {@code resultIfCancelled} method should not be used as the returned value of a calculation
 * directly, since {@link #appliesTo(String...)} must be checked before returning any value.
 * The reason for this is that the canceller might not be able to cancel every calculation it receives, therefore you
 * need to be sure that it is the case before cancelling the current calculation.
 *
 * @author Antoine Tran
 */
public interface CalculationCanceller extends CalculationAffection {

    /**
     * Calculates the result of a given input, assuming the affection is compatible with it. <p>
     * If the input is not guaranteed to be compatible (in other words, "modifiable"), the {@link #appliesTo(String...)}
     * implemented method shall be checked first.
     * @param input a given compatible input, according to the cancelling behavior
     * @return the result calculated from the input given
     */
    String resultIfCancelled(String... input);
}
