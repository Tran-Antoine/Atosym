package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Objects;

public class Expression {

    private final String expression;
    private ExpressionElement[] monomials;

    public Expression(float value) {
        this.monomials = new ExpressionElement[]{new NumberElement(value)};
        this.expression = join(monomials);
    }

    public Expression(String expression) {
        this.expression = Objects.requireNonNull(expression);
        this.monomials = simpleAnalyze();
    }

    public Expression(ExpressionElement singleElement) {
        this.monomials = new ExpressionElement[]{singleElement};
        this.expression = singleElement.getExpression();
    }

    public Expression(ExpressionElement[] monomials) {
        this.monomials = monomials;
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
                    new Monomial(1, new Variable[]{
                            new Variable(expression.charAt(0), null, null)
                    })
            };
        }

        throw new RuntimeException("Unreachable statement : couldn't identify the simple expression given : "+expression);
    }

    public Expression simpleSum(Expression other) {

        Monomial firstA = (Monomial) monomials[0];
        Monomial secondA = (Monomial) other.monomials[0];

        if(firstA.hasSameVariablePartAs(secondA)) {
            // TODO : Use BigDecimal
            float floatResult = firstA.getNumericValue() + secondA.getNumericValue();
            return new Expression(new Monomial(floatResult, firstA.getVariables()));
        }
        return new Expression(new ExpressionElement[]{firstA, secondA});
    }

    public Expression simpleMult(Expression other) {
        Monomial firstA = (Monomial) monomials[0];
        Monomial secondA = (Monomial) monomials[0];

        Variable[] firstVars = firstA.getVariables();
        Variable[] secondVars = secondA.getVariables();

        float floatResult = firstA.getNumericValue() * secondA.getNumericValue();
        return new Expression(new Monomial(floatResult, Variable.combine(firstVars, secondVars)));
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
}
