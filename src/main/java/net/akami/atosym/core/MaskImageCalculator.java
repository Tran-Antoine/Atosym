package net.akami.atosym.core;

import net.akami.atosym.utils.DisplayUtils;
import net.akami.atosym.utils.ExpressionUtils;
import net.akami.atosym.utils.FastAtosymMath;

import java.util.Map;

/**
 * Operator used for images of functions computation. <br>
 * With a given expression, it replaces all the variables from a map by their mapped value. <br>
 */
public class MaskImageCalculator implements MaskOperator<Map<Character, String>> {

    @Override
    public void compute(Mask in, Mask out, Map<Character, String> extraData, MaskContext context) {
        String input = in.getExpression();
        for (Character c : extraData.keySet()) {
            input = replace(c, extraData.get(c), input);
        }
        String finalResult = FastAtosymMath.reduce(input, context);
        out.reload(finalResult);
    }

    /**
     * Replaces all occurrences of {@code var} in {@code self} by {@code value}. <br>
     * It manages to keep the replacement mathematically logic. For instance, replacing {@code x} by {@code 3}
     * in {@code 4x} will give {@code 4*3} instead of {@code 43}
     *
     * @param var   the var to be replaced
     * @param value the value used to replace the var
     * @param self  the given expression
     * @return a new expression with replaced values
     */
    public String replace(char var, String value, String self) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < self.length(); i++) {
            char currentChar = self.charAt(i);

            if (currentChar == var) {
                if (i != 0 && ExpressionUtils.NUMBERS.contains(String.valueOf(self.charAt(i - 1)))) {
                    //the char before the variable is a number. 4x obviously means 4*x
                    builder.append("*");
                } // No number before the variable, for instance 3+x
                DisplayUtils.surroundWithParenthesis(builder, value);
            } else {
                // No variable found, we just addBranch the same char
                builder.append(currentChar);
            }
        }
        return builder.toString();
    }
}
