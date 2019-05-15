package net.akami.mask.expression;

public class SimpleFraction implements ExpressionElement {

    private final ExpressionElement numerator;
    private final Expression denominator;
    private final String expression;

    public SimpleFraction(float numerator, Expression denominator) {
        this(new NumberElement(numerator), denominator);
    }

    public SimpleFraction(ExpressionElement numerator, Expression denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.expression = loadExpression();
    }

    private String loadExpression() {

        StringBuilder builder = new StringBuilder();
        boolean numRequiresBrackets = true;
        boolean denRequiresBrackets = true;

        if(numerator instanceof Monomial) {
            numRequiresBrackets = ((Monomial) numerator).requiresBrackets();
        }

        if(denominator.length() == 1) {
            ExpressionElement first = denominator.get(0);
            if(first instanceof Monomial) {
                denRequiresBrackets = ((Monomial) first).requiresBrackets();
            }
        }

        if(numRequiresBrackets) builder.append('(');
        builder.append(numerator.getExpression());
        if(numRequiresBrackets) builder.append(')');
        builder.append('/');
        if(denRequiresBrackets) builder.append('(');
        builder.append(denominator.toString());
        if(denRequiresBrackets) builder.append(')');

        return builder.toString();
    }

    @Override
    public String getExpression() {
        return expression;
    }

    public boolean hasSameDenominatorAs(SimpleFraction other) {
        return denominator.equals(other.denominator);
    }

    public ExpressionElement getNumerator() {
        return numerator;
    }

    public Expression getDenominator() {
        return denominator;
    }
}
