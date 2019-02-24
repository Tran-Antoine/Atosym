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
     * Call {@link MaskOperator#imageFor(MaskExpression, MaskExpression, boolean, String...)} with the mask specified in the begin call
     * as the out parameter. The boolean parameter doesn't matter, since in = out.
     * See the method itself for further information.
     * @return the operator itself for chaining
     */
    public MaskOperator imageFor(String... values) {
        if(mask == null)
            throw new MaskException("Unable to operate with the defined expression", null);
        // setOut doesn't matter, because in = out
        return imageFor(mask, mask, false, values);
    }

    /**
     * Calculates an image from the values given. The result can be a numerical or polynomial expression, depending
     * of the values given. For instance, {3, 2} as values for the expression {x + y} will give 5, while {3} for the
     * same expression will give {3 + y}
     * <br/>
     * Note that if the default mask is null, it will be set to the out parameter before the calculations.
     * @throws IllegalStateException if more values than variables are given.
     * @param out the affected mask
     * @param setToOut whether the next calculation will be done from the out expression or not
     * @param values the values replacing the variables
     * @return the operator itself for chaining.
     */
    public MaskOperator imageFor(MaskExpression in, MaskExpression out, boolean setToOut, String... values) {

        if(this.mask == null) {
            this.mask = out;
        }
        if(out == null) {
            out = MaskExpression.TEMP;
        }

        if (mask.getVariablesAmount() < values.length) {
            throw new IllegalStateException("More values than variables given");
        }

        String toReplace = in.getExpression();
        for (int i = 0; i < values.length; i++) {
            char var = in.getVariables()[i];
            toReplace = replace(var, values[i], toReplace);
        }
        out.reload(ReducerFactory.reduce(toReplace));
        if(setToOut) {
            this.mask = out;
        }
        return this;
    }

    private String replace(char var, String value, String self) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < self.length(); i++) {
            if (self.charAt(i) == var) {

                if (i != 0 && NON_VARIABLES.contains(String.valueOf(self.charAt(i - 1)))) {
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

    /*
    Code not ready for the release.

    public MaskOperator differentiate(char var) {
        return differentiate(mask, mask, var, true);
    }

    public MaskOperator differentiate(MaskExpression in, MaskExpression out, char var, boolean setToOut) {

        String[] values = new String[in.getVariablesAmount()];

        for(int i = 0; i < in.getVariables().length; i++) {
            if(in.getVariables()[i] == var)
                values[i] = String.valueOf(var);
            else {
                values[i] = String.valueOf(1);
            }
        }

        imageFor(in, out, true, values);
        return this;
    }*/

    /**
     * Call {@link MaskOperator#reduce(MaskExpression, MaskExpression)} with the mask specified in the last begin call as the out
     * parameter. See the method itself for further information.
     * @return the operator itself for chaining
     */
    public MaskOperator reduce() {
        if(mask == null)
            throw new MaskException("Unable to operate with the defined expression :", null);
        return reduce(mask, mask);
    }

    /**
     * Reduces the MaskExpression given, if possible.
     * <br/>
     * For instance, 4x + 3x + 3 + 5 will be reduced as 7x + 8
     * @param out the mask to reduce
     * @return the operator itself for chaining
     */
    public MaskOperator reduce(MaskExpression in, MaskExpression out) {
        out.reload(ReducerFactory.reduce(out.getExpression()));
        this.mask = out;
        return this;
    }

    /**
     * Converts the current mask as an expression. The mask won't be affected by the call of this method.
     * @return "null" if begin() hasn't been called after the last end() call, otherwise the expression of the mask.
     */
    public String asExpression() {
        return asExpression(this.mask);
    }

    public String asExpression(MaskExpression mask) {
        return mask == null ? "null" : mask.getExpression();
    }

    /**
     * Converts the current mask as an expression. The mask won't be affected by the call of this method.
     * @throws MaskException if the expression cannot be casted as an integer
     * @return NaN if begin() hasn't been called after the last end() call, otherwise the int value of the expression.
     */
    public int asInt() {
        return asInt(this.mask);
    }

    public int asInt(MaskExpression mask) {
        if(mask.getVariablesAmount() != 0)
            throw new MaskException("Cannot convert the expression to an integer", mask);
        return (int) Float.parseFloat(mask.getExpression());
    }

    public float asFloat() {
        return asFloat(this.mask);
    }

    public float asFloat(MaskExpression mask) {
        return Float.parseFloat(mask.getExpression());
    }

    public MaskExpression getMask() {
        return mask;
    }
}
