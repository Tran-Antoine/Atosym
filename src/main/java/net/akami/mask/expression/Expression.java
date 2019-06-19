package net.akami.mask.expression;

import net.akami.mask.core.MaskContext;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A representation of any possible mathematical expression. <p>
 * The expression class is at the top of the expression hierarchy. An expression is composed of monomials,
 * which themselves are composed of a coefficient and a variable part. <p>
 * However, expressions might also be found at the bottom of the hierarchy. In fact, some overlays
 * are nothing else than subclasses of Expression. They are used by variables, which are at the bottom of the
 * expression hierarchy. These overlays are thus at the very bottom of the initial architecture, but are also at the top
 * of the new hierarchy they created. Thereby, expressions can indirectly contain other expressions. <p>
 *
 * Expression creation can be performed in two ways :
 *
 * <ul>
 *     <li> Creation through string analysis. Basically, it constructs an expression by analyzing a string directly.
 *          This is exclusively available for expressions at the bottom of binary trees. They indeed require very low
 *          analysis resources. There are three possibilities :
 *          <ul>
 *              <li> The expression is a number </li>
 *              <li> The expression is a char, thus an unknown </li>
 *              <li> The expression is a symbol matching a trigonometric function </li>
 *          </ul>
 *     </li>
 *
 *     <li> Creation through existing data. No analysis is required, the expression is simply loaded from data.
 *          Data can be of three different types :
 *          <ul>
 *              <li> A numeric value </li>
 *              <li> A char, along with its {@link MaskContext} </li>
 *              <li> A monomial or a list of monomials </li>
 *          </ul>
 *     </li>
 * </ul>
 *
 * @author Antoine Tran
 */
public class Expression implements Cloneable {

    protected final String expression;
    protected final List<Monomial> elements;
    protected final MaskContext context;

    /**
     * Constructs an expression from a numeric value
     * @param value the value representing the whole expression
     */
    protected Expression(float value) {
        this(value, MaskContext.DEFAULT);
    }

    /**
     * Constructs an expression from a numeric value
     * @param value the value representing the whole expression
     * @param context the calculation environment the expression belong to
     */
    protected Expression(float value, MaskContext context) {
        this.context = context;
        this.elements = Collections.singletonList(new NumberElement(value));
        this.expression = join(elements);
    }

    /**
     * Constructs an expression from a list of monomials
     * @param elements the list of monomials
     */
    public Expression(List<Monomial> elements) {
        this(elements, MaskContext.DEFAULT);
    }

    /**
     * Constructs an expression from a list of monomials
     * @param elements the list of monomials
     * @param context the calculation environment the expression belong to
     */
    public Expression(List<Monomial> elements, MaskContext context) {
        this.context = context;
        this.elements = Collections.unmodifiableList(elements);
        this.expression = join(this.elements);
    }

    /**
     * Constructs an expression from a sequence of monomials
     * @param elements the list of monomials
     */
    public Expression(Monomial... elements) {
        this(MaskContext.DEFAULT, elements);
    }

    /**
     * Constructs an expression from a sequence of monomials
     * @param context the calculation environment the expression belong to
     * @param elements the list of monomials
     */
    public Expression(MaskContext context, Monomial... elements) {
        this.context = context;
        this.elements = Arrays.asList(elements);
        this.expression = join(this.elements);
    }

    /**
     * Constructs an expression through string analysis. Only bottom branches' expressions are allowed
     * @param expression the string to analyze
     */
    public Expression(String expression) {
        this(expression, MaskContext.DEFAULT);
    }

    /**
     * Constructs an expression through string analysis. Only bottom branches' expressions are allowed
     * @param expression the string to analyze
     * @param context the calculation environment the expression belong to
     */
    public Expression(String expression, MaskContext context) {
        this.context = context;
        this.expression = zeroIfEmpty(Objects.requireNonNull(expression));
        this.elements = Collections.singletonList(simpleAnalyze());
    }

    /**
     * Constructs an expression from a unique monomial
     * @param singleElement the unique monomial
     */
    public Expression(Monomial singleElement) {
        this(singleElement, MaskContext.DEFAULT);
    }

    /**
     * Constructs an expression from a unique monomial
     * @param singleElement the unique monomial
     * @param context the calculation environment the expression belong to
     */
    public Expression(Monomial singleElement, MaskContext context) {
        this.context = context;
        this.elements = Collections.unmodifiableList(Collections.singletonList(singleElement));
        this.expression = zeroIfEmpty(elements.size() == 1 ? elements.get(0).getExpression() : join(this.elements));
    }

    /**
     * Constructs an expression from a numeric value
     * @param value the value representing the whole expression
     * @return a new expression
     */
    public static Expression of(float value) {
        return of(value, MaskContext.DEFAULT);
    }

    /**
     * Constructs an expression from a numeric value
     * @param value the value representing the whole expression
     * @param context the calculation environment the expression belong to
     * @return a new expression
     */
    public static Expression of(float value, MaskContext context) {
        return new Expression(value, context);
    }

    /**
     * Constructs an expression from a unique monomial
     * @param singleElement the unique monomial
     * @return a new expression
     */
    public static Expression of(Monomial singleElement) {
        return of(singleElement, MaskContext.DEFAULT);
    }

    /**
     * Constructs an expression from a unique monomial
     * @param singleElement the unique monomial
     * @param context the calculation environment the expression belong to
     * @return a new expression
     */
    public static Expression of(Monomial singleElement, MaskContext context) {
        return new Expression(singleElement, context);
    }

    /**
     * Constructs an expression from a unique simple variable, under the form of a char
     * @param var the char being the variable
     * @return a new expression
     */
    public static Expression of(char var) {
        return of(var, MaskContext.DEFAULT);
    }

    /**
     * Constructs an expression from a unique simple variable, under the form of a char
     * @param var the char being the variable
     * @param context the calculation environment the expression belong to
     * @return a new expression
     */
    public static Expression of(char var, MaskContext context) {
        return Expression.of(new Monomial(1, new SingleCharVariable(var, context)));
    }

    private Monomial simpleAnalyze() {

        if(ExpressionUtils.isANumber(expression))
            return new NumberElement(Float.parseFloat(expression));

        if(expression.length() > 1)
            throw new RuntimeException("Unsolvable statement reached : trying to simpleAnalyze a non-simple expression : "+expression);

        if(MaskContext.DEFAULT.getFunctionByExpression(expression).isPresent())
            throw new RuntimeException("Unsupported yet");//return new FunctionSign(expression.charAt(0), context);

        if(expression.matches("[a-zA-DF-Z]")) {
            return new Monomial(1, new SingleCharVariable(expression.charAt(0), context));
        }

        throw new RuntimeException("Unreachable statement : couldn't identify the simple expression given : "+expression);
    }

    private String join(List<Monomial> elements) {
        return zeroIfEmpty(ExpressionUtils.chainElements(elements, Monomial::getExpression));
    }

    /**
     * @return whether the object's expression is the same as the obj's expression
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Expression))
            return false;

        return this.expression.equals(((Expression) obj).expression);
    }

    /**
     * @return whether the expression as a whole requires brackets if mixed with others
     */
    public boolean requiresBrackets() {
        return elements.size() != 1 || elements.get(0).requiresBrackets();
    }

    @Override
    public String toString() {
        return expression;
    }

    /**
     * Some expressions might have been loaded with a single monomial, a 1 coefficient, a single variable
     * being intricate, with no overlays. If that's the case, the monomials forming the unique variable will be
     * logically returned instead.
     * @return the monomials forming the expression
     */
    public List<Monomial> getElements() {
        if(length() == 1 && get(0).getVarPart().size() == 1 && get(0).getNumericValue() == 1) {
            VariablePart uniquePart = get(0).getVarPart();
            if(uniquePart.get(0).getOverlaysSize() == 0)
                return uniquePart.get(0).getElements();
        }
        return elements;
    }

    /**
     * @return the amount of monomials the expression contains
     */
    public int length() {
        return elements.size();
    }

    /**
     * Used to retrieve the monomial at a given index
     * @param index the given index
     * @return the monomial at the given index
     */
    public Monomial get(int index) {
        return elements.get(index);
    }

    @Override
    public Object clone() {
        return new Expression(getElements());
    }

    private String zeroIfEmpty(String input) {
        return input.isEmpty() ? "0" : input;
    }

    /**
     * @return the maximal exponent detected through the monomials forming the expression
     */
    public float getMaximalPower() {
        float max = 0;

        for(Monomial m : elements) {
            for(Variable var : m.getVarPart()) {
                float exponent = var.getFinalExponent().orElse(1f);
                if(exponent > max) max = exponent;
            }
        }
        return max;
    }
}
