package net.akami.mask.function;

import net.akami.mask.utils.ExpressionUtils;

public interface TrigonometryOperation {

    UnaryOperation getOperation();

    default String trigonometryOperation(String input, char opChar) {
        if(ExpressionUtils.isANumber(input)) {
            double result = getOperation().compute(Double.valueOf(input));
            return String.valueOf(result > 10E-15 ? result : 0);
        }
        // TODO : check, should there be more () ?
        return "("+input+")"+opChar;
    }

    @FunctionalInterface
    interface UnaryOperation {
        double compute(double d);
    }
}
