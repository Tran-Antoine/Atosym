package net.akami.mask.operation;

import net.akami.mask.exception.MaskException;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;

/**
 * MaskExpression is the core object of the library's operation system. It handles a String, which corresponds to the expression,
 * and an array of variables, used to solve the expression for values, or to provide images of the function.
 *
 * It is a mutable class, hence the expression can be modified. When doing some calculations with an expression, you
 * will be asked to choose the original expression (in) plus the expression that will be affected by the
 * calculation (out).
 */
public class MaskExpression {

    /**
     * Temporary instance that can be used when only the findResult of an expression is needed, to avoid multiple instances.
     * <br/>
     * Be aware that once you did a calculation with it by setting it as the out parameter, if you
     * want the int value of TEMP for instance, you need to call {@link MaskHandler#asExpression(MaskExpression)}
     * and not {@link MaskHandler#asExpression()}, otherwise you'll get the non-temporary expression
     * you based yourself on for the calculation.
     * <br/>
     * Here is an example :
     *
     * <pre>
     * MaskExpression base = new MaskExpression(2x);
     * MaskHandler operator = MaskHandler.begin(base);
     * String exp = operator.imageFor(MaskExpression.TEMP, false, 5).asExpression();
     * System.out.println(exp);
     *
     * Output : "2x"
     *
     * -----------------
     *
     * MaskExpression base = new MaskExpression(2x);
     * MaskHandler operator = MaskHandler.begin(base);
     * String exp = operator.imageFor(MaskExpression.TEMP, false, 5).asExpression(MaskExpression.TEMP);
     * System.out.println(exp);
     *
     * Output : "10"
     * </pre>
     */
    public static final MaskExpression TEMP = new MaskExpression();

    private String expression;
    private char[] variables;

    /**
     * Constructs a new MaskExpression without any string expression by default.
     */
    public MaskExpression() {
        this(null);
    }

    /**
     * Constructs a new MaskExpression from the given string.
     * @param expression the given string
     */
    public MaskExpression(String expression) {
        reload(expression);
    }

    private char[] createVariables() {
        return ExpressionUtils.toVariablesType(expression).toCharArray();
    }

    public int getVariablesAmount(){ return variables.length; }
    public String getExpression()  { return expression;       }
    public char[] getVariables()   { return variables;        }

    public void reload(String newExp) {
        if(newExp == null) {
            this.expression = "undefined";
            this.variables = new char[]{};
        } else {
            this.expression = FormatterFactory.formatTrigonometry(FormatterFactory.addMultiplicationSigns(newExp
                    .replaceAll("\\s", ""), false));
            this.expression = FormatterFactory.removeMultiplicationSigns(this.expression);
            System.out.println("Expression found : "+this.expression);
            checkExpressionValidity();
            this.variables = createVariables();
        }
    }

    private void checkExpressionValidity() {
        if(expression.length() == 0)
            return;
        if(".*/^".contains(String.valueOf(expression.charAt(0)))
                || ".+-*/^".contains(String.valueOf(expression.charAt(expression.length()-1)))
                || !expression.matches("[a-zA-Z0-9.+\\-*/^()]+"))
            throw new MaskException("Expression not valid", this);
    }

    /**
     * @return the expression of the mask
     */
    @Override
    public String toString() {
        return expression;
    }
}
