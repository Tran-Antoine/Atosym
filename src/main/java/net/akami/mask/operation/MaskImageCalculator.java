package net.akami.mask.operation;

import net.akami.mask.affection.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.ReducerFactory;

import java.util.Map;

public class MaskImageCalculator implements MaskOperator<Map<Character, String>> {

    @Override
    public void compute(MaskExpression in, MaskExpression out, Map<Character, String> extraData, MaskContext context) {
        String input = in.getExpression();
        for(Character c : extraData.keySet()) {
            input = replace(c, extraData.get(c), input);
        }
        out.reload(ReducerFactory.reduce(input, context));
    }

    public String replace(char var, String value, String self) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < self.length(); i++) {
            if (self.charAt(i) == var) {

                if (i != 0 && ExpressionUtils.NUMBERS.contains(String.valueOf(self.charAt(i - 1)))) {
                    //the char before the variable is a number. 4x obviously means 4*x
                    builder.append("*" +"("+value+")");
                } else {
                    // No number before the variable, for instance 3+x
                    builder.append("("+value+")");
                }
            } else {
                // No variable found, we just add the same char
                builder.append(self.charAt(i));
            }
        }
        return builder.toString();
    }
}
