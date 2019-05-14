package net.akami.mask.expression;

import java.math.BigDecimal;

public class SimpleFraction implements ExpressionElement<SimpleFraction> {

    private final Monomial numerator;
    private final Monomial denominator;

    public SimpleFraction(Monomial numerator, Monomial denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public String getExpression() {
        StringBuilder builder = new StringBuilder();
        boolean numRequiresBrackets = numerator.getNumericValue() != 1 && numerator.getNumericValue() != -1;
        boolean denRequiresBrackets = denominator.getNumericValue() != 1 && denominator.getNumericValue() != -1;

        if(numRequiresBrackets) builder.append('(');
        builder.append(numerator.getExpression());
        if(numRequiresBrackets) builder.append(')');
        builder.append('/');
        if(denRequiresBrackets) builder.append('(');
        builder.append(denominator.getExpression());
        if(denRequiresBrackets) builder.append(')');

        return builder.toString();
    }

    @Override
    public boolean isMergeable(SimpleFraction other) {
        return hasSameDenominatorAs(other) && numerator.hasSameVariablePartAs(other.numerator);
    }

    @Override
    public SimpleFraction mergeElement(SimpleFraction other) {

        BigDecimal b1 = new BigDecimal(numerator.getNumericValue());
        BigDecimal b2 = new BigDecimal(other.numerator.getNumericValue());
        float floatResult = b1.add(b2).floatValue();

        Monomial newNumerator = new Monomial(floatResult, numerator.getVariables());
        return new SimpleFraction(newNumerator, denominator);
    }

    public boolean hasSameDenominatorAs(SimpleFraction other) {
        return denominator.equals(other.denominator);
    }

    public Monomial getNumerator() {
        return numerator;
    }

    public Monomial getDenominator() {
        return denominator;
    }
}
