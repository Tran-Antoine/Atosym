package net.akami.mask.math;

import net.akami.mask.exception.MaskException;

import java.util.ArrayList;

/**
 * MaskExpression is the core object of the mask library. It handles a String, which corresponds to the expression,
 * and an array of variables, used to solve the expression for values, or to provide images of the function.
 *
 * It is a mutable class, hence the expression can be modified. When doing some calculations with an expression, you
 * will be asked to choose the original expression (in) plus the expression that will be affected by the
 * calculation(out).
 */
public class MaskExpression {

    private String expression;
    private char[] variables;

    public MaskExpression() {
        this(null);
    }

    public MaskExpression(String expression) {
        reload(expression);
    }

    protected char[] createVariables() {
        String letters = expression.replaceAll("\\d", "");
        ArrayList<Character> chars = new ArrayList<>();
        for(char c : letters.toCharArray()) {
            if(!chars.contains(c) && !MaskOperator.NON_VARIABLES.contains(""+c))
                chars.add(c);
        }
        char[] vars = new char[chars.size()];
        for(Character c : chars) {
            vars[chars.indexOf(c)] = c;
        }
        return vars;
    }

    public int getVariablesAmount() { return variables.length; }
    public String getExpression()   { return expression;       }
    public char[] getVariables()    { return variables;        }

    public void reload(String newExp) {
        if(newExp == null) {
            this.expression = "undefined";
            this.variables = new char[]{};
        } else {
            this.expression = newExp.replaceAll("\\s", "");
            checkExpressionValidity();
            this.variables = createVariables();
        }
    }

    private void checkExpressionValidity() {
        boolean valid = true;

        if(".*/^".contains(String.valueOf(expression.charAt(0))))
            valid = false;
        if(".+-*/^".contains(String.valueOf(expression.charAt(expression.length()-1))))
            valid = false;

        if(!valid)
            throw new MaskException("Expression not valid", this);
    }
    @Override
    public String toString() {
        return expression;
    }
}
