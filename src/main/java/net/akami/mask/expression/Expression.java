package net.akami.mask.expression;

import net.akami.mask.encapsulator.CompleteCoverEncapsulator;
import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.function.MathFunction;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Expression implements ExpressionEncapsulator, Cloneable {

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
        return Expression.of(new Monomial(1, new SimpleVariable(var, null, null)));
    }

    public Expression(List<ExpressionElement> elements) {
        this.elements = Collections.unmodifiableList(elements);
        this.expression = join(this.elements);
    }

    public Expression(String expression) {
        this.expression = Objects.requireNonNull(expression);
        this.elements = Collections.singletonList(simpleAnalyze());
    }

    public Expression(ExpressionElement... elements) {
        this.elements = Arrays.asList(elements);
        this.expression = elements.length == 1 ? elements[0].getExpression() : join(this.elements);
    }

    private ExpressionElement simpleAnalyze() {

        if(ExpressionUtils.isANumber(expression))
            return new NumberElement(Float.parseFloat(expression));

        if(expression.length() > 1)
            throw new RuntimeException("Unsolvable statement reached : trying to simpleAnalyze a non-simple expression : "+expression);

        if(MathFunction.getByExpression(expression).isPresent())
            return new FunctionSign(expression.charAt(0));

        if(expression.matches("[a-zA-DF-Z]")) {
            return new Monomial(1, new SimpleVariable(expression.charAt(0), null, null));
        }

        throw new RuntimeException("Unreachable statement : couldn't identify the simple expression given : "+expression);
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

    private String join(List<ExpressionElement> elements) {
        return ExpressionUtils.chainElements(elements, ExpressionElement::getExpression);
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

    @Override
    public Object clone() {
        return new Expression(getElements());
    }
}
