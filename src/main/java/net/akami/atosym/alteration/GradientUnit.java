package net.akami.atosym.alteration;

import net.akami.atosym.utils.ExpressionUtils;

import java.math.BigDecimal;

/**
 * Specifies that angles are given in gradients. Conversions will be performed whenever a scalar value is detected. <br>
 * Be aware that you must be careful about where you add this modifier. If by mistake you add it to a binary operator,
 * every scalar value will be converted, even though binary operators don't work with angles.
 */
public class GradientUnit implements IOCalculationModifier<Expression> {

    public static final double CONVERSION_TO_RADIANS = 0.015707963267949D;

    @Override
    public Expression[] modify(Expression... input) {
        Expression[] output = new Expression[1];
        BigDecimal inputDecimal = new BigDecimal(input[0].toString());
        BigDecimal conversion = new BigDecimal(CONVERSION_TO_RADIANS);
        output[0] = Expression.of(inputDecimal.multiply(conversion).floatValue());
        return output;
    }

    @Override
    public boolean appliesTo(Expression... input) {
        return ExpressionUtils.isANumber(input[0]);
    }

    @Override
    public float priorityLevel() {
        return 2;
    }
}
