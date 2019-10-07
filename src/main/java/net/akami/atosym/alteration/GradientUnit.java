package net.akami.atosym.alteration;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Specifies that angles are given in gradients. Conversions will be performed whenever a scalar value is detected. <br>
 * Be aware that you must be careful about where you addBranch this modifier. If by mistake you addBranch it to a binary operator,
 * every scalar value will be converted, even though binary operators don't work with angles.
 */
public class GradientUnit implements IOCalculationModifier<MathObject> {

    public static final double CONVERSION_TO_RADIANS = 0.015707963267949D;

    @Override
    public List<MathObject> modify(List<MathObject> input) {
        NumberExpression single = (NumberExpression) input.get(0);
        BigDecimal inputDecimal = new BigDecimal(single.getValue());
        BigDecimal conversion = new BigDecimal(CONVERSION_TO_RADIANS);
        return Collections.singletonList(new NumberExpression(inputDecimal.multiply(conversion).floatValue()));
    }

    @Override
    public boolean appliesTo(List<MathObject> input) {
        if(input.size() != 1) {
            throw new UnsupportedOperationException("DegreeUnit is not capable of handling non unary functions");
        }
        return input.get(0).getType() == MathObjectType.NUMBER;
    }

    @Override
    public float priorityLevel() {
        return 2;
    }
}
