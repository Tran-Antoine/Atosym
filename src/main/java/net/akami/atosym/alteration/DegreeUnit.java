package net.akami.atosym.alteration;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.ExpressionUtils;

import java.util.List;

/**
 * Specifies that angles are given in degrees. Conversions will be performed whenever a scalar value is detected. <br>
 * Be aware that you must be careful about where you add this modifier. If by mistake you add it to a binary operator,
 * every scalar value will be converted, even though binary operators don't work with angles.
 */
public class DegreeUnit implements IOCalculationModifier<MathObject> {

    @Override
    public List<MathObject> modify(List<MathObject> input) {
        //output[0] = Expression.of((float) Math.toRadians(Float.parseFloat(input[0].toString())));
        return null;
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
