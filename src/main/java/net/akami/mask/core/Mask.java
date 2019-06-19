package net.akami.mask.core;

import net.akami.mask.utils.FormatterFactory;

/**
 * Warning : Please not that most of the examples from this javadoc file are invalid, and will be rewritten soon. <p></p>
 *
 * Mask is the core object of the library's operation system. It handles a String, which corresponds to the expression,
 * and an array of variables, used to solve the expression for values, or to provide images of the function.
 *
 * It is a mutable class, hence the expression can be modified. When doing some calculations with an expression, you
 * will be asked to choose the original expression (in) plus the expression that will be affected by the
 * calculation (out).
 */
public class Mask {

    /**
     * Temporary instance that can be used when only the findResult of an expression is needed, to avoid multiple instances.
     * <br/>
     * Be aware that once you did a calculation with it by setting it as the out parameter, if you
     * want the int value of TEMP for instance, you need to call {@link MaskOperatorHandler#asExpression(Mask)}
     * and not {@link MaskOperatorHandler#asExpression()}, otherwise you'll getElement the non-temporary expression
     * you based yourself on for the calculation.
     * <br/>
     * Here is an example :
     *
     * <pre>
     * Mask base = new Mask(2x);
     * MaskOperatorHandler operator = MaskOperatorHandler.begin(base);
     * String exp = operator.imageFor(Mask.TEMP, false, 5).asExpression();
     * System.out.println(exp);
     *
     * Output : "2x"
     *
     * -----------------
     *
     * Mask base = new Mask(2x);
     * MaskOperatorHandler operator = MaskOperatorHandler.begin(base);
     * String exp = operator.imageFor(Mask.TEMP, false, 5).asExpression(Mask.TEMP);
     * System.out.println(exp);
     *
     * Output : "10"
     * </pre>
     */
    public static final Mask TEMP = new Mask();

    private String expression;

    /**
     * Constructs a new Mask without any string expression by default.
     */
    public Mask() {
        this(null);
    }

    /**
     * Constructs a new Mask from the given string.
     * @param expression the given string
     */
    public Mask(String expression) {
        reload(expression);
    }


    public String getExpression()  { return expression;       }

    /**
     * Reloads the mask with the given expression. If {@code null}, the new expression will be "undefined". <p>
     * Validity checks are also performed through reload.
     * @param newExp the new expression for the mask
     */
    public void reload(String newExp) {
        if(newExp == null) {
            this.expression = "undefined";
        } else {
            this.expression = newExp;
            checkExpressionValidity();
        }
    }

    private void checkExpressionValidity() {
        MaskContext.DEFAULT.assertExpressionValidity(this);
    }

    /**
     * @return the expression of the mask
     */
    @Override
    public String toString() {
        return expression;
    }
}
