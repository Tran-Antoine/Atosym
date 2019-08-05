package net.akami.atosym.overlay;

import net.akami.atosym.expression.Expression;
import net.akami.atosym.expression.Monomial;

import java.util.List;

public class ExponentOverlay extends Expression implements ExpressionOverlay {

    public static final ExponentOverlay NULL_FACTOR = new ExponentOverlay(1);
    public static final ExponentOverlay SQUARED = new ExponentOverlay(2);

    public ExponentOverlay(float numericValue) {
        super(numericValue);
    }

    public ExponentOverlay(List<Monomial> elements) {
        super(elements);
    }

    public static ExponentOverlay fromExpression(Expression self) {
        return new ExponentOverlay(self.getElements());
    }

    @Override
    public String[] getEncapsulationString(List<Monomial> elements, int index, List<ExpressionOverlay> others) {
        if(this.equals(NULL_FACTOR)) return new String[]{"", ""};

        Monomial first;
        boolean endNoBrackets = length() == 1 && (first = this.elements.get(0)) != null
                && !first.requiresBrackets();

        boolean beginNoBrackets = false;

        if (elements.size() == 1 && elements.get(0) != null) {
            Monomial exponent = elements.get(0);
            if(!exponent.requiresBrackets()) beginNoBrackets = true;

            if(!beginNoBrackets && index != 0) {
                if(others.get(index-1) instanceof CompleteCoverOverlay) beginNoBrackets = true;
            }
        }


        String begin = beginNoBrackets ? "" : "(";
        String half = beginNoBrackets  ? "" : ")";
        String formattedEnd = endNoBrackets ? this.expression : '('+ this.expression +')';
        return new String[]{begin, half+'^'+formattedEnd};
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ExponentOverlay)) return false;
        return super.equals(obj);
    }
}
