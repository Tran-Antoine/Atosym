package net.akami.mask.encapsulator;

import net.akami.mask.expression.Expression;
import net.akami.mask.expression.ExpressionElement;
import net.akami.mask.expression.Monomial;

import java.util.List;

public class ExponentEncapsulator extends Expression implements ExpressionEncapsulator {

    public ExponentEncapsulator(float numericValue) {
        super(numericValue);
    }

    public ExponentEncapsulator(List<ExpressionElement> elements) {
        super(elements);
    }

    public static ExponentEncapsulator fromExpression(Expression self) {
        return new ExponentEncapsulator(self.getElements());
    }

    @Override
    public String[] getEncapsulationString(List<ExpressionElement> elements, int index, List<ExpressionEncapsulator> others) {
        ExpressionElement first;
        boolean endNoBrackets = length() == 1 && (first = this.elements.get(0)) instanceof Monomial
                && !((Monomial) first).requiresBrackets();

        boolean beginNoBrackets = false;

        if (elements.size() == 1 && elements.get(0) instanceof Monomial) {
            Monomial exponent = (Monomial) elements.get(0);
            if(!exponent.requiresBrackets()) beginNoBrackets = true;

            if(!beginNoBrackets && index != 0) {
                if(others.get(index-1) instanceof CompleteCoverEncapsulator) beginNoBrackets = true;
            }
        }


        String begin = beginNoBrackets ? "" : "(";
        String half = beginNoBrackets  ? "" : ")";
        String formattedEnd = endNoBrackets ? this.expression : '('+ this.expression +')';
        return new String[]{begin, half+'^'+formattedEnd};
    }
}
