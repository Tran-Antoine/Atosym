package net.akami.mask.overlay;

import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;

import java.util.List;

public class FractionEncapsulator extends Expression implements ExpressionOverlay {

    public FractionEncapsulator(float numericValue) {
        super(numericValue);
    }

    public FractionEncapsulator(List<Monomial> elements) {
        super(elements);
    }

    public static ExponentEncapsulator fromExpression(Expression self) {
        return new ExponentEncapsulator(self.getElements());
    }

    @Override
    public String[] getEncapsulationString(List<Monomial> elements, int index, List<ExpressionOverlay> others) {
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
