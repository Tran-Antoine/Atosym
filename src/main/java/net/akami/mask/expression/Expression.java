package net.akami.mask.expression;

import net.akami.mask.core.MaskContext;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Expression implements Cloneable {

    protected final String expression;
    protected final List<ExpressionElement> elements;
    protected final MaskContext context;

    protected Expression(float value) {
        this(value, MaskContext.DEFAULT);
    }

    protected Expression(float value, MaskContext context) {
        this.context = context;
        this.elements = Collections.singletonList(new NumberElement(value));
        this.expression = join(elements);
    }

    public Expression(List<ExpressionElement> elements) {
        this(elements, MaskContext.DEFAULT);
    }

    public Expression(List<ExpressionElement> elements, MaskContext context) {
        this.context = context;
        this.elements = Collections.unmodifiableList(elements);
        this.expression = join(this.elements);
    }

    public Expression(ExpressionElement... elements) {
        this(MaskContext.DEFAULT, elements);
    }

    public Expression(MaskContext context, ExpressionElement... elements) {
        this.context = context;
        this.elements = Arrays.asList(elements);
        this.expression = join(this.elements);
    }

    public Expression(String expression) {
        this(expression, MaskContext.DEFAULT);
    }

    public Expression(String expression, MaskContext context) {
        this.context = context;
        this.expression = Objects.requireNonNull(expression);
        this.elements = Collections.singletonList(simpleAnalyze());
    }

    public Expression(ExpressionElement singleElement) {
        this(singleElement, MaskContext.DEFAULT);
    }

    public Expression(ExpressionElement singleElement, MaskContext context) {
        this.context = context;
        this.elements = Collections.unmodifiableList(Collections.singletonList(singleElement));
        this.expression = elements.size() == 1 ? elements.get(0).getExpression() : join(this.elements);
    }

    public static Expression of(float value) {
        return of(value, MaskContext.DEFAULT);
    }

    public static Expression of(float value, MaskContext context) {
        return new Expression(value, context);
    }

    public static Expression of(ExpressionElement singleElement) {
        return of(singleElement, MaskContext.DEFAULT);
    }

    public static Expression of(ExpressionElement singleElement, MaskContext context) {
        return new Expression(singleElement, context);
    }

    public static Expression of(char var) {
        return of(var, MaskContext.DEFAULT);
    }

    public static Expression of(char var, MaskContext context) {
        return Expression.of(new ExpressionElement(1, new SimpleVariable(var, null, null)));
    }

    private ExpressionElement simpleAnalyze() {

        if(ExpressionUtils.isANumber(expression))
            return new NumberElement(Float.parseFloat(expression));

        if(expression.length() > 1)
            throw new RuntimeException("Unsolvable statement reached : trying to simpleAnalyze a non-simple expression : "+expression);

        if(MaskContext.DEFAULT.getFunctionByExpression(expression).isPresent())
            return new FunctionSign(expression.charAt(0), context);

        if(expression.matches("[a-zA-DF-Z]")) {
            return new ExpressionElement(1, new SimpleVariable(expression.charAt(0), null, null));
        }

        throw new RuntimeException("Unreachable statement : couldn't identify the simple expression given : "+expression);
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

    public boolean requiresBrackets() {
        return elements.size() != 1 || elements.get(0).requiresBrackets();
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
