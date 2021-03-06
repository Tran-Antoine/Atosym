package net.akami.atosym.expression;

import net.akami.atosym.overlay.ExponentOverlay;
import net.akami.atosym.overlay.ExpressionOverlay;
import net.akami.atosym.overlay.FractionOverlay;
import net.akami.atosym.utils.ExpressionUtils;

import java.util.*;

/**
 * A mathematical unknown containing overlays, encapsulating a series of irreducible monomials. <br>
 * It handles :
 *
 * <ul>
 * <li> A list of {@link ExpressionOverlay}s, being the overlays encapsulating the variable
 * <li> A list of {@link Monomial}s, being the elements encapsulated inside the overlays
 * </ul>
 *
 * See {@link Variable} for examples
 *
 * @author Antoine Tran
 */
public class IntricateVariable implements Variable, Cloneable {

    private final List<ExpressionOverlay> overlays;
    private final List<Monomial> elements;
    private String finalExpression;

    /**
     * Constructs an intricate variable with no overlays, and a single monomial. This is used only for conversion from monomial
     * to variable, when variable merges are performed. Some properties work with operators, which means they compute
     * list of monomials. This constructor is used to convert these monomials to variables through method referencing.
     * @param singlePart the unique monomial encapsulated into the variable
     */
    public IntricateVariable(Monomial singlePart) {
        this(Collections.singletonList(singlePart), Collections.emptyList());
    }

    /**
     * Constructs an intricate variable from a unique monomial and a unique overlay.
     * @param singlePart the monomial encapsulated into the variable
     * @param singleOverlay the overlay encapsulating the variable
     */
    public IntricateVariable(Monomial singlePart, ExpressionOverlay singleOverlay) {
        this(Collections.singletonList(singlePart), Collections.singletonList(singleOverlay));
    }

    /**
     * Constructs an intricate variable with a unique overlay, and a given list of monomials.
     * @param parts the monomials encapsulated into the variable
     * @param singleOverlay the unique overlay encapsulating the monomials
     */
    public IntricateVariable(List<Monomial> parts, ExpressionOverlay singleOverlay) {
        this(parts, Collections.singletonList(singleOverlay));
    }

    /**
     * Constructs an intricate variable with a list of overlays, and a list of monomials.
     * @param parts the monomials encapsulated into the variable
     * @param layers the overlays encapsulating the variable
     */
    public IntricateVariable(List<Monomial> parts, List<ExpressionOverlay> layers) {
        this.elements = Collections.unmodifiableList(Objects.requireNonNull(parts));
        this.overlays = Collections.unmodifiableList(Objects.requireNonNull(layers));
        if(parts.size() > 1 && layers.size() == 0)
            throw new IllegalArgumentException("Attempting to group multi variables into one complex");
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Variable)) return false;
        Variable other = (Variable) obj;
        return getElements().equals(other.getElements()) && getAbsoluteOverlays().equals(other.getAbsoluteOverlays());
    }

    @Override
    public int getElementsSize() {
        return elements.size();
    }

    @Override
    public int getOverlaysSize() {
        return overlays.size();
    }

    @Override
    public String getExpression() {
        if(finalExpression == null)
            finalExpression = loadExpression();
        return finalExpression;
    }

    private String loadExpression() {
        return ExpressionUtils.encapsulate(elements, overlays);
    }

    /**
     * Whatever start or end put, no {@link IndexOutOfBoundsException} can be thrown.
     */
    @Override
    public List<ExpressionOverlay> getOverlaysSection(int start, int end) {
        int length = getOverlaysSize();
        int realStart = start < 0 ? length + start : start;
        int realEnd = end < 0 ? length + end : end;

        if(realStart < 0 || realStart >= length || realEnd < 0 || realEnd >= length) {
            return Collections.emptyList();
        }

        List<ExpressionOverlay> finalElements = new ArrayList<>(realEnd);
        for(int i = realStart; i <= realEnd; i++)
            finalElements.add(overlays.get(i));
        return finalElements;
    }

    @Override
    public boolean isFraction() {
        return getOverlaysSize() > 0 && getOverlay(-1) instanceof FractionOverlay;
    }

    /**
     * @return empty if the last overlay is not a float exponent, otherwise the exponent
     */
    @Override
    public Optional<Float> getFinalExponent() {
        if(getOverlaysSize() == 0) return Optional.empty();
        if(!(getOverlay(-1) instanceof ExponentOverlay)) return Optional.empty();

        ExponentOverlay last = (ExponentOverlay) getOverlay(-1);
        if(ExpressionUtils.isANumber(last)) {
            return Optional.of(last.getElements().get(0).getNumericValue());
        }
        return Optional.empty();
    }

    @Override
    public List<Monomial> uncover(int amount) {
        /* If there are several layers, that means that getElements() will skip the other layers.
         * Therefore, we need to check it*/
        if(getOverlaysSize() > amount) {
            List<ExpressionOverlay> newOverlays = getOverlaysSection(0, -amount);
            Monomial singleMonomial = new Monomial(1, new IntricateVariable(getElements(), newOverlays));
            return Collections.singletonList(singleMonomial);
        }

        if(getOverlaysSize() < amount) throw new IllegalArgumentException(
                "Attempting to uncover more layers than there actually are");

        return elements;
    }

    @Override
    public List<Monomial> getNumerator() {
        if(isFraction())
            return uncover(1);
        return getElements();
    }

    @Override
    public List<Monomial> getDenominator() {
        if(isFraction())
            return ((FractionOverlay) getOverlay(-1)).getElements();
        return Collections.singletonList(NumberElement.MULT_DIV_NULL_FACTOR);
    }

    /**
     * @return the overlays encapsulating the variable
     */
    public List<ExpressionOverlay> getOverlays() {
        return overlays;
    }

    @Override
    public List<Monomial> getElements() {
        if(elements.size() == 1) {
            VariablePart unique = elements.get(0).getVarPart();
            if(unique.size() == 1 && unique.get(0).getOverlaysSize() == 0)
                return unique.get(0).getElements();
        }
        return elements;
    }

    @Override
    public ExpressionOverlay getOverlay(int i) {
        if(getOverlaysSize() == 0 && getElementsSize() == 1) {
            VariablePart unique = elements.get(0).getVarPart();
            if(unique.size() == 1)
                return unique.get(0).getOverlay(i);
        }

        if(i < 0) return overlays.get(getOverlaysSize() + i);
        else return overlays.get(i);
    }

    @Override
    public Object clone() {
        return new IntricateVariable(getElements(), getOverlays());
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public char getVar() {
        Monomial single = elements.get(0);
        if(single.getExpression().length() == 1)
            return single.getExpression().charAt(0);
        if(single.getVarPart().size() == 0) return ' ';
        return single.getVarPart().get(0).getVar();
    }

    @Override
    public List<ExpressionOverlay> getAbsoluteOverlays() {
        return getOverlays();
    }
}
