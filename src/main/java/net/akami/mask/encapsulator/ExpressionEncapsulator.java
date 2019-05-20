package net.akami.mask.encapsulator;

import net.akami.mask.expression.ExpressionElement;

import java.util.List;

public interface ExpressionEncapsulator {

    String[] getEncapsulationString(List<ExpressionElement> elements, int index, List<ExpressionEncapsulator> others);

}
