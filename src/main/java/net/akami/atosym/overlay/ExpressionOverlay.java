package net.akami.atosym.overlay;

import net.akami.atosym.expression.Monomial;

import java.util.List;

public interface ExpressionOverlay {

    String[] getEncapsulationString(List<Monomial> elements, int index, List<ExpressionOverlay> others);

}
