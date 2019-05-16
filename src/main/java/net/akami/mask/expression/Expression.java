package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Expression {

    private final String expression;
    private final List<ExpressionElement> elements;

    private Expression(float value) {
        this.elements = Collections.singletonList(new NumberElement(value));
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
        this.elements = Collections.singletonList(simpleAnalyze());
    }

    public Expression(ExpressionElement... monomials) {
        this.elements = Arrays.asList(monomials);
        this.expression = join(elements);
    }

    private ExpressionElement simpleAnalyze() {

        if(ExpressionUtils.isANumber(expression))
            return new NumberElement(Float.parseFloat(expression));

        if(expression.length() > 1)
            throw new RuntimeException("Unsolvable statement reached : trying to simpleAnalyze a non-simple expression : "+expression);

        if(MathFunction.getByExpression(expression).isPresent())
            return new FunctionSign(expression.charAt(0));

        if(expression.matches("[a-zA-DF-Z]")) {
            return new Monomial(1, new Variable(expression.charAt(0), null, null));
        }

        throw new RuntimeException("Unreachable statement : couldn't identify the simple expression given : "+expression);
    }

    private String join(List<ExpressionElement> monomials) {
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

    public List<ExpressionElement> getElements() {
        return elements;
    }

    public int length() {
        return elements.size();
    }

    public ExpressionElement get(int index) {
        return elements.get(index);
    }
}
