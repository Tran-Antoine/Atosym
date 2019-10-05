package net.akami.atosym.alteration;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.utils.ExpressionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Specifies that angles are given in gradients. Conversions will be performed whenever a scalar value is detected. <br>
 * Be aware that you must be careful about where you add this modifier. If by mistake you add it to a binary operator,
 * every scalar value will be converted, even though binary operators don't work with angles.
 */
public class GradientUnit implements IOCalculationModifier<MathObject> {

    public static final double CONVERSION_TO_RADIANS = 0.015707963267949D;

    @Override
    public List<MathObject> modify(List<MathObject> input) {
        BigDecimal inputDecimal = new BigDecimal(input.get(0).toString());
        BigDecimal conversion = new BigDecimal(CONVERSION_TO_RADIANS);
        return Collections.singletonList(new NumberExpression(inputDecimal.multiply(conversion).floatValue()));
    }

    @Override
    public boolean appliesTo(List<MathObject> input) {
        return ExpressionUtils.isANumber(input.get(0).toString());
    }

    @Override
    public float priorityLevel() {
        return 2;
    }
}
