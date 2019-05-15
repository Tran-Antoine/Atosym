package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Objects;

public class Expression {

    private final String expression;
    private final ExpressionElement[] elements;

    private Expression(float value) {
        this.elements = new ExpressionElement[]{new NumberElement(value)};
        this.expression = join(elements);
    }

    public static Expression of(float value) {
        return new Expression(value);
    }

    public static Expression of(ExpressionElement singleElement) {
        return new Expression(singleElement);
    }

    public static Expression of(char var) {
        return Expression.of(new Monomial(1, new Variable(var, null, null)));
    }

    public Expression(String expression) {
        this.expression = Objects.requireNonNull(expression);
        this.elements = simpleAnalyze();
    }

    private Expression(ExpressionElement singleElement) {
        this.elements = new ExpressionElement[]{singleElement};
        this.expression = singleElement.getExpression();
    }

    public Expression(ExpressionElement... monomials) {
        this.elements = monomials;
        this.expression = join(monomials);
    }

    private ExpressionElement[] simpleAnalyze() {

        if(ExpressionUtils.isANumber(expression))
            return new ExpressionElement[]{new NumberElement(Float.parseFloat(expression))};

        if(expression.length() > 1)
            throw new RuntimeException("Unsolvable statement reached : trying to simpleAnalyze a non-simple expression : "+expression);

        if(MathFunction.getByExpression(expression).isPresent())
            return new ExpressionElement[]{new FunctionSign(expression.charAt(0))};

        if(expression.matches("[a-zA-DF-Z]")) {
            return new ExpressionElement[]{
                    new Monomial(1, new Variable(expression.charAt(0), null, null))
            };
        }

        throw new RuntimeException("Unreachable statement : couldn't identify the simple expression given : "+expression);
    }

    private String join(ExpressionElement[] monomials) {
        StringBuilder builder = new StringBuilder();
        for(ExpressionElement element : monomials) {
            String expression = element.getExpression();
            if(builder.length() == 0 || ExpressionUtils.isSigned(expression)) {
                builder.append(expression);
            } else {
                builder.append('+').append(expression);
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Expression))
            return false;

        return this.expression.equals(((Expression) obj).expression);
    }

    @Override
    public String toString() {
        return expression;
    }

    public ExpressionElement[] getElements() {
        return elements;
    }

    public int length() {
        return elements.length;
    }

    public ExpressionElement get(int index) {
        return elements[index];
    }
}
