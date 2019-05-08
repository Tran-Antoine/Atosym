package net.akami.mask.expression;

public class Fraction implements ExpressionElement {

    private Expression numerator;
    private Expression denominator;

    public Fraction(Expression numerator, Expression denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    // TODO : add brackets
    @Override
    public String getExpression() {
        return numerator.toString() + "/" + denominator.toString();
    }
}
