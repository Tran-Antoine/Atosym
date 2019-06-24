package net.akami.mask.alteration;

import net.akami.mask.expression.Expression;
import net.akami.mask.utils.ExpressionUtils;

import java.math.BigDecimal;

public class GradientUnit implements IOCalculationModifier<Expression> {

    @Override
    public Expression[] modify(Expression... input) {
        Expression[] output = new Expression[1];
        BigDecimal inputDecimal = new BigDecimal(input[0].toString());
        BigDecimal conversion = new BigDecimal(0.015707963267949);
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
