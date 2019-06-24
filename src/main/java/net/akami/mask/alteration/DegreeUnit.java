package net.akami.mask.alteration;

import net.akami.mask.expression.Expression;
import net.akami.mask.utils.ExpressionUtils;

/**
 * Specifies that angles are given in degrees. Conversions will be performed whenever an angle is detected.
 */
public class DegreeUnit implements IOCalculationModifier<Expression> {

    @Override
    public Expression[] modify(Expression... input) {
        Expression[] output = new Expression[1];
        output[0] = Expression.of((float) Math.toRadians(Float.parseFloat(input[0].toString())));
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
