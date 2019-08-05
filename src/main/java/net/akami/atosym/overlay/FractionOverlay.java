package net.akami.atosym.overlay;

import net.akami.atosym.expression.Expression;
import net.akami.atosym.expression.Monomial;

import java.util.List;

public class FractionOverlay extends Expression implements ExpressionOverlay {

    public static final FractionOverlay FRACTION_NULL_FACTOR = new FractionOverlay(1);

    public FractionOverlay(float numericValue) {
        super(numericValue);
    }

    public FractionOverlay(List<Monomial> elements) {
        super(elements);
    }

    public static FractionOverlay fromExpression(Expression self) {
        return new FractionOverlay(self.getElements());
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FractionOverlay)) return false;
        return super.equals(obj);
    }
}
