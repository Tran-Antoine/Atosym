package net.akami.mask.expression;

import net.akami.mask.overlay.ExpressionOverlay;

import java.util.List;
import java.util.Optional;

public interface Variable {

    String getExpression();
    List<ExpressionOverlay> getAbsoluteOverlays();
    ExpressionOverlay getOverlay(int i);
    List<ExpressionOverlay> getOverlaysSection(int start, int end);
    int getOverlaysSize();
    int getElementsSize();
    List<Monomial> getElements();
    List<Monomial> getNumerator();
    List<Monomial> getDenominator();
    List<Monomial> uncover(int amount);
    Optional<Float> getFinalExponent();
    boolean isFraction();
    boolean elementsEqual(Variable other);
    char getVar();
}
