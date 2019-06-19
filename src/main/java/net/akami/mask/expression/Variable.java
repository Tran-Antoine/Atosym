package net.akami.mask.expression;

import net.akami.mask.overlay.ExpressionOverlay;

import java.util.List;
import java.util.Optional;

/**
 * An immutable mathematical unknown. Two implementation of this interface exist, being {@link SingleCharVariable} and
 * {@link IntricateVariable}. <p>
 * A list of variables goes along with a coefficient, both forming a monomial. Complex variables are recursive, by
 * handling a list of monomials, which also contain variables. The end of the recursivity chain
 * is made by single char variables, which are simple unknown such as "x" or "y", or by {@link NumberElement}s, a
 * kind of monomial that does not contain any unknown part. <p>
 * Examples :
 *
 * <li> {@code z} is a {@link SingleCharVariable}, with no overlay except the implicit {@code ^1} exponent. Its list
 * of monomials has a size of 1, the only monomial being 1x
 * <li> {@code sin(a+b)} is a {@link IntricateVariable}, with one overlay being {@code sin}, and two elements (monomials)
 * being {@code a} and {@code b}. These elements are both monomials with a SingleCharVariable as their unique variable.
 * @author Antoine Tran
 */
public interface Variable {

    /**
     * For display only. Note that all methods returning strings should not be used for algebraic treatment at all. <p>
     * They are used recursively by the {@code getExpression()} method from the {@link Expression} class, to print
     * the final result of the calculation
     * @return the literal form of the variable, {@code x} or {@code sin(y^2)} for instance.
     */
    String getExpression();

    /**
     * Used to retrieve all the overlays of the variable, including the implicit {@code ^1} exponent.
     * @return the list of all overlays encapsulating the current variable.
     */
    List<ExpressionOverlay> getAbsoluteOverlays();

    /**
     * Used to retrieve the overlay at the given index. <p>
     * At least in the implementations provided by the library, negative indexes are supported.
     * Therefore, 0 will give the first element, -1 the last one, -2 the penultimate one, etc. <p>
     * Note that the only index supported for {@link SingleCharVariable}s is {@code -1}, being the implicit {@code ^1} exponent.
     * @param i the index of the list of overlays
     * @return the overlay present at the given index
     */
    ExpressionOverlay getOverlay(int i);

    /**
     * Used to retrieve a section of the list of overlays, from a given start (included) to a given end (included).
     * Calling this method from a {@link SingleCharVariable} will always return an empty list. If you want to retrieve
     * all overlays from a SingleCharVariable including the explicit one, use {@link #getAbsoluteOverlays()} instead.
     * @param start the start of the section wanted
     * @param end the end of the section wanted
     * @return a list of overlays containing exclusively the overlays with an index included in [start;end]
     */
    List<ExpressionOverlay> getOverlaysSection(int start, int end);

    /**
     * @return how many overlays encapsulate the variable
     */
    int getOverlaysSize();

    /**
     * @return how many elements are encapsulated in the variable (always 1 for {@link SingleCharVariable}s).
     */
    int getElementsSize();

    /**
     * Note : all getters return immutable elements.
     * @return the list of elements encapsulated in the variable
     */
    List<Monomial> getElements();

    /**
     * Used to retrieve the numerator of the variable. If the last overlay is not a fraction, then {@link #getElements()}
     * will be returned. If it is, {@code uncover(-1)} will be returned.
     * @return the numerator of the variable
     */
    List<Monomial> getNumerator();

    /**
     * Used to retrieve the denominator of the variable. If the last overlay is not a fraction, the implicit {@code 1}
     * divider will be returned. If it is, the elements forming the last overlay will be returned.
     * @return the denominator of the variable
     */
    List<Monomial> getDenominator();

    /**
     * Used to retrieve a list of monomials corresponding to the variable without a given amount of overlays. If
     * the amount given is exactly the amount of overlays, {@link #getElements()} will be returned. If the amount given
     * is lower than the amount of overlays, a single monomial with a 1 coefficient and the variable without the last overlay
     * will be returned. If the amount of given is greater than the amount of overlays, throws an error.
     * @param amount the amount of overlays to fake remove in order to get the list of monomials.
     * @return a list of monomials corresponding to the actual variable without {@code amount} overlays
     */
    List<Monomial> uncover(int amount);

    /**
     * Used to retrieve the global exponent of the variable. If the last overlay is an exponent, and if the last overlay
     * is a number, then returns {@code Optional.of(number)}. Otherwise, returns an empty optional
     * @return the global exponent of the variable
     */
    Optional<Float> getFinalExponent();

    /**
     * @return whether the last overlay of the variable is a fraction or not
     */
    boolean isFraction();

    /**
     * @return the literal char of the variable. Complex variables use recursivity through their elements to determine
     * the char
     */
    char getVar();
}
