package net.akami.atosym.alteration;

import net.akami.atosym.utils.ExpressionUtils;

/**
 * Specifies that angles are given in degrees. Conversions will be performed whenever a scalar value is detected. <br>
 * Be aware that you must be careful about where you add this modifier. If by mistake you add it to a binary operator,
 * every scalar value will be converted, even though binary operators don't work with angles.
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
