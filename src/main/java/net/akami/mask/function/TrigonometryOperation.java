package net.akami.mask.function;

import net.akami.mask.utils.ExpressionUtils;

/**
 * Util interface avoiding repetitions inside the trigonometric functions. <p>
 * Might not be required anymore after the expression system is finished.
 */
public interface TrigonometryOperation {

    /**
     * The trigonometric function matching the class. <p>
     * Should be either {@code Math::sin}, {@code Math::cos} or {@code Math::tan}
     * @return
     */
    UnaryOperation getOperation();

    /**
     * Computes a trigonometric operation from the given input and the given binding. <p>
     * Does not compute anything if the input is not a number.
     * @param input the input given, to which the operation is to be applied
     * @param opChar the binding corresponding to the trigonometric operation
     * @return the result of the calculation
     */
    default String trigonometricOperation(String input, char opChar) {
        if(ExpressionUtils.isANumber(input)) {
            double result = getOperation().compute(Double.valueOf(input));
            return String.valueOf(Math.abs(result) > 10E-15 ? result : 0);
        }
        // TODO : check, should there be more () ?
        return "("+input+")"+opChar;
    }

    @FunctionalInterface
    interface UnaryOperation {
        double compute(double d);
    }
}
