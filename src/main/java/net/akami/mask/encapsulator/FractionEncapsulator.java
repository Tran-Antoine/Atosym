package net.akami.mask.encapsulator;

import net.akami.mask.expression.Expression;
import net.akami.mask.expression.ExpressionElement;

import java.util.List;

public class FractionEncapsulator extends Expression implements ExpressionEncapsulator {

    public FractionEncapsulator(float numericValue) {
        super(numericValue);
    }

    public FractionEncapsulator(List<ExpressionElement> elements) {
        super(elements);
    }

    public static ExponentEncapsulator fromExpression(Expression self) {
        return new ExponentEncapsulator(self.getElements());
    }

    @Override
    public String[] getEncapsulationString(List<ExpressionElement> elements, int index, List<ExpressionEncapsulator> others) {
        String[] result = new String[2];

        boolean insightsNeedsBrackets = elements.size() > 1 || elements.get(0).requiresBrackets();

        result[0] =  insightsNeedsBrackets ? "(" : "";
        String insightsEnd = insightsNeedsBrackets ? ")/" : "/";

        boolean selfNeedsBrackets = requiresBrackets();

        String selfExpression = selfNeedsBrackets ? '(' + this.toString() + ")" : this.toString();

        result[1] = insightsEnd + selfExpression;
        return result;
    }
}
