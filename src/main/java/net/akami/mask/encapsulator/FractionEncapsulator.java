package net.akami.mask.encapsulator;

import net.akami.mask.expression.ExpressionElement;

import java.util.List;

public class FractionEncapsulator implements ExpressionEncapsulator {
    @Override
    public String[] getEncapsulationString(List<ExpressionElement> elements, int index, List<ExpressionEncapsulator> others) {
        return new String[0];
    }
}
