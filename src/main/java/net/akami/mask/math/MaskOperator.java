package net.akami.mask.math;

import net.akami.mask.exception.MaskException;
import net.akami.mask.utils.ReducerFactory;

public class MaskOperator {

    public static final String NON_VARIABLES = "0123456789+-*=^.";
    private static final MaskOperator instance = new MaskOperator();

    private MaskExpression mask;

    /**
     * Gets the static instance of {@link MaskOperator}, though no {@link MaskExpression} will be used by default
     * @return the static instance of {@link MaskOperator}
     */
    public static MaskOperator begin() {
        return begin(null);
    }
    /**
     * Defines which mask will be affected by the calculations by default, when none is specified. If not called,
     * you'll have to specify the affected mask each time you want to make an operation, or a MaskException will be
     * thrown.
     * @param mask the mask affected
     * @return the static instance of {@link MaskOperator}
     */
    public static MaskOperator begin(MaskExpression mask) {
        instance.mask = mask;
        return instance;
    }

    /**
     * Sets the affected mask to null. Calling end() locks the expression given in the previous begin() call, so
     * that it won't be affected but will throw an exception next time a calculation is done without any begin() call
     * nor any "out" specification.
     */
    public void end() {
        this.mask = null;
    }

    /**
     * Call {@link MaskOperator#imageFor(MaskExpression, float...)} with the mask specified in the begin call as the
     * out parameter. See the method itself for further information.
     * @return the operator itself for chaining
     */
    public MaskOperator imageFor(float... values) {
        if(mask == null)
            throw new MaskException("Unable to operate with the defined expression", null);
        return imageFor(mask, values);
    }

    /**
     * Calculates an image from the values given. The result can be a numerical or polynomial expression, depending
     * of the values given. For instance, {3, 2} as values for the expression {x + y} will give 5, while {3} for the
     * same expression will give {3 + y}
     * @throws IllegalStateException if more values than variables are given.
     * @param out the affected mask
     * @param values the values replacing the variables
     * @return the operator itself for chaining.
     */
    public MaskOperator imageFor(MaskExpression out, float... values) {

        if (mask.getVariablesAmount() < values.length) {
            throw new IllegalStateException("More values than variables given");
        }

        String replaced = mask.getExpression();
        for (int i = 0; i < values.length; i++) {
            char var = mask.getVariables()[i];
            replaced = replace(var, values[i], replaced);
        }
        out.reload(ReducerFactory.reduce(replaced));
        this.mask = out;
        return this;
    }

    private String replace(char var, float value, String self) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < self.length(); i++) {
            if (self.charAt(i) == var && i != 0) {

                if (NON_VARIABLES.contains(String.valueOf(self.charAt(i - 1)))) {
                    //the char before the variable is a number. 4x obviously means 4*x
                    builder.append("*" + value);
                } else {
                    // No number before the variable, for instance 3+x
                    builder.append(value);
                }
            } else {
                // No variable found, we just add the same char
                builder.append(self.charAt(i));
            }
        }
        return builder.toString();
    }

    public MaskOperator reduce() {
        if(mask == null)
            throw new MaskException("Unable to operate with the defined expression :", null);
        return reduce(mask);
    }

    public MaskOperator reduce(MaskExpression out) {
        out.reload(ReducerFactory.reduce(out.getExpression()));
        this.mask = out;
        return this;
    }

    /**
     * Converts the current mask as an expression. The mask won't be affected by the call of this method.
     * @return "null" if begin() hasn't been called after the last end() call, otherwise the expression of the mask.
     */
    public String asExpression() {
        return mask == null ? "null" : mask.getExpression();
    }

    /**
     * Converts the current mask as an expression. The mask won't be affected by the call of this method.
     * @throws MaskException if the expression cannot be casted as an integer
     * @return NaN if begin() hasn't been called after the last end() call, otherwise the int value of the expression.
     */
    public int asInt() {
        if(mask.getVariablesAmount() != 0)
            throw new MaskException("Cannot convert the expression to an integer", mask);
        return (int) Float.parseFloat(mask.getExpression());
    }

    public float asFloat() {
        return Float.parseFloat(mask.getExpression());
    }

    public MaskExpression getMask() {
        return mask;
    }
}
