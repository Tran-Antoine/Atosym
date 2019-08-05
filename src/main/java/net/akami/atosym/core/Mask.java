package net.akami.atosym.core;

import net.akami.atosym.check.ValidityCheck;
import net.akami.atosym.expression.Expression;

/**
 * Mutable object representing a raw mathematical expression as a string, without any extra information. <br>
 * Masks are used by a {@link MaskOperatorHandler} as containers for string results. <br>
 * The Mask class is different from the {@link Expression} class. A Mask is a simple object handling a string that can be
 * changed through operations, whereas an Expression is an immutable data class containing the whole mathematical translation
 * of the string. Expressions are exclusively used along with the binary tree algorithm, for calculations. <br>
 * Masks can be created without initial value (String). Modifications are performed through {@link #reload(String)}. <br><br>
 *
 * Whenever an expression is defined for the current atosym, validity checks are performed. They ensure that the given
 * expression is valid mathwise. {@link ValidityCheck} are defined in the {@link MaskContext} the atosym belongs to.
 *
 * @author Antoine Tran
 */
public class Mask {

    /**
     * Temporary instance that can be used whenever you perform operations, and don't need the result to be stored
     * inside a Mask. This is useful if you just want to retrieve the result of a given operation.
     * It allows multiple instances avoidance. <br>
     * Example : <pre>
     * {@code
     * Mask curve = new Mask("x^2 + 3x + 4");
     * MaskOperatorHandler handler = MaskOperatorHandler.DEFAULT;
     * // Value already set to true by default, if not changed
     * handler.setCurrentToOut(true);
     * String derivative = handler.compute(MaskDerivativeCalculator.class, curve, Mask.TEMP, 'x')
     *         .asExpression();
     *
     * System.out.println(derivative);
     * } </pre>
     * Output : {@code 2.0x + 3.0}
     */
    public static final Mask TEMP = new Mask();

    private String expression;
    private MaskContext context;

    /**
     * Constructs a new Mask without any string expression by default, and without any specified context.
     * {@link MaskContext#DEFAULT} will be used.
     */
    public Mask() {
        this(MaskContext.DEFAULT);
    }
    /**
     * Constructs a new Mask without any string expression by default.
     * @param context the MaskContext the atosym belongs to
     */
    public Mask(MaskContext context) {
        this(null, context);
    }

    /**
     * Constructs a new Mask from the given string, without any specified context.
     * {@link MaskContext#DEFAULT} will be used.
     * @param expression the given string
     */
    public Mask(String expression) {
        this(expression, MaskContext.DEFAULT);
    }
    /**
     * Constructs a new Mask from the given string.
     * @param expression the given string
     * @param context    the MaskContext the atosym belongs to
     */
    public Mask(String expression, MaskContext context) {
        this.context = context;
        reload(expression);
    }

    /**
     * @return the mathematical expression that the atosym handles
     */
    public String getExpression()  {
        return expression;
    }

    /**
     * Reloads the atosym with the given expression. If {@code null}, the new expression will be "undefined". <br>
     * Validity checks are also performed through reload.
     * @param newExp the new expression for the atosym
     */
    public void reload(String newExp) {
        if(newExp == null) {
            this.expression = "undefined";
        } else {
            this.expression = newExp.replaceAll("\\s", "");
            checkExpressionValidity();
        }
    }

    private void checkExpressionValidity() {
        context.assertExpressionValidity(this);
    }

    /**
     * @return the expression of the atosym
     */
    @Override
    public String toString() {
        return expression;
    }
}
