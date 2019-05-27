package net.akami.mask.overlay;

import net.akami.mask.expression.Monomial;

import java.util.List;

public interface ExpressionOverlay {

    String[] getEncapsulationString(List<Monomial> elements, int index, List<ExpressionOverlay> others);

}
