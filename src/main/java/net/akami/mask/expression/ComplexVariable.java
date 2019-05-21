package net.akami.mask.expression;

import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.utils.ExpressionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ComplexVariable implements Variable<ComplexVariable>, Cloneable {

    private final List<ExpressionOverlay> overlays;
    private final List<Monomial> elements;
    private String finalExpression;

    public ComplexVariable(List<Monomial> parts) {
        this(parts, Collections.emptyList());
    }

    public ComplexVariable(List<Monomial> parts, List<ExpressionOverlay> layers) {
        this.elements = Collections.unmodifiableList(Objects.requireNonNull(parts));
        this.overlays = Collections.unmodifiableList(Objects.requireNonNull(layers));

        if(layers.size() == 0)
            throw new IllegalArgumentException
                    ("Cannot create a composed variable with no layer. Use multiple SimpleVariables instead");
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ComplexVariable)) return false;

        ComplexVariable other = (ComplexVariable) obj;

        if(this.elementsLength() != other.elementsLength()) return false;
        if(!overlays.equals(other.overlays)) return false;
        return elements.equals(other.elements);
        /*for(int i = 0; i < this.elementsLength(); i++) {
            Monomial v1 = getElement(i);
            Monomial v2 = other.getElement(i);

            if(!v1.equals(v2)) return false;
        }

        return true;*/
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

    public List<ExpressionOverlay> getOverlaysFraction(int index) {
        int realIndex = index < 0 ? overlaysLength() - index : index;

        List<ExpressionOverlay> finalElements = new ArrayList<>(realIndex);
        for(int i = 0; i < realIndex; i++)
            finalElements.add(overlays.get(i));
        return finalElements;
    }

    @Override
    public int compareTo(ComplexVariable o) {
        return 0;
    }

    public List<ExpressionOverlay> getOverlays() {
        return overlays;
    }

    public List<Monomial> getElements() {
        return elements;
    }

    public ExpressionOverlay getLayer(int i) {
        if(i < 0) return overlays.get(overlays.size()-i);
        else return overlays.get(i);
    }

    @Override
    public Object clone() {
        return new ComplexVariable(getElements());
    }
}
