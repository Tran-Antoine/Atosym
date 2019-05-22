package net.akami.mask.expression;

import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.utils.ExpressionUtils;

import java.util.*;

public class ComplexVariable implements Variable, Cloneable {

    private final List<ExpressionOverlay> overlays;
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

    public int elementsLength() {
        return elements.size();
    }

    public int overlaysLength() {
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

    public List<ExpressionOverlay> getOverlaysFraction(int start, int end) {
        int realStart = start < 0 ? overlaysLength() + start : start;
        int realEnd = end < 0 ? overlaysLength() + end : end;

        List<ExpressionOverlay> finalElements = new ArrayList<>(realEnd);
        for(int i = realStart; i < realEnd; i++)
            finalElements.add(overlays.get(i));
        return finalElements;
    }

    @Override
    public int compareTo(Variable o) {
        if(overlaysLength() == 0) return 0;
        Optional<Float> currentValue = getFinalExponent();

        if(o instanceof SingleCharVariable) {
            if(currentValue.orElse(-1f) == 1) return 0;
            else return -1;
        }

        Optional<Float> oValue = ((ComplexVariable) o).getFinalExponent();
        if(!oValue.isPresent()) return -1;
        if(!currentValue.isPresent()) return 1;

        return Float.compare(currentValue.get(), oValue.get());
    }

    /**
     * @return empty if the last overlay is not a float exponent, otherwise the exponent
     */
    public Optional<Float> getFinalExponent() {
        if(overlaysLength() == 0) return Optional.empty();
        if(!(getOverlay(-1) instanceof ExponentOverlay)) return Optional.empty();

        ExponentOverlay last = (ExponentOverlay) getOverlay(-1);
        if(ExpressionUtils.isANumber(last)) {
            return Optional.of(last.getElements().get(0).getNumericValue());
        }
        return Optional.empty();
    }

    public List<ExpressionOverlay> getOverlays() {
        return overlays;
    }

    @Override
    public List<Monomial> getElements() {
        return elements;
    }

    public ExpressionOverlay getOverlay(int i) {
        if(i < 0) return overlays.get(overlays.size()+i);
        else return overlays.get(i);
    }

    @Override
    public Object clone() {
        return new ComplexVariable(getElements());
    }
}
