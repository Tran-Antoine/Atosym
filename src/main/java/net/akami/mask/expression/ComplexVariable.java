package net.akami.mask.expression;

import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.utils.ExpressionUtils;

import java.util.*;

public class ComplexVariable implements Variable, Cloneable {

    private final List<ExpressionOverlay> overlays;
    // TODO : handle an Expression
    private final List<Monomial> elements;
    private String finalExpression;

    public ComplexVariable(Monomial singlePart) {
        this(Collections.singletonList(singlePart));
    }

    public ComplexVariable(List<Monomial> parts) {
        this(parts, Collections.emptyList());
    }

    public ComplexVariable(List<Monomial> parts, ExpressionOverlay singleOverlay) {
        this(parts, Collections.singletonList(singleOverlay));
    }

    public ComplexVariable(Monomial singlePart, ExpressionOverlay singleOverlay) {
        this(Collections.singletonList(singlePart), Collections.singletonList(singleOverlay));
    }

    public ComplexVariable(List<Monomial> parts, List<ExpressionOverlay> layers) {
        this.elements = Collections.unmodifiableList(Objects.requireNonNull(parts));
        this.overlays = Collections.unmodifiableList(Objects.requireNonNull(layers));
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Variable)) return false;
        return getExpression().equals(((Variable) obj).getExpression());
    }

    public boolean elementsEqual(ComplexVariable other) {
        return this.elements.equals(other.elements);
    }

    @Override
    public int getElementsSize() {
        return elements.size();
    }

    @Override
    public int getOverlaysSize() {
        return overlays.size();
    }

    public Monomial getElement(int index) {
        return elements.get(index);
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
            Monomial singleMonomial = new Monomial(1, new ComplexVariable(getElements(), newOverlays));
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
            VariablePart unique = getElement(0).getVarPart();
            if(unique.size() == 1)
                return unique.get(0).getOverlay(i);
        }

        if(i < 0) return overlays.get(getOverlaysSize() + i);
        else return overlays.get(i);
    }

    @Override
    public Object clone() {
        return new ComplexVariable(getElements());
    }

    @Override
    public String toString() {
        return getExpression();
    }
}
