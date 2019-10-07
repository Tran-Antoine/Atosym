package net.akami.atosym.alteration;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;

import java.util.Collections;
import java.util.List;

/**
 * Specifies that angles are given in degrees. Conversions will be performed whenever a scalar value is detected. <br>
 * Be aware that you must be careful about where you addBranch this modifier. If by mistake you addBranch it to a binary operator,
 * every scalar value will be converted, even though binary operators don't work with angles.
 */
public class DegreeUnit implements IOCalculationModifier<MathObject> {

    @Override
    public List<MathObject> modify(List<MathObject> input) {
        NumberExpression single = (NumberExpression) input.get(0);
        MathObject result = new NumberExpression((float) Math.toRadians(single.getValue()));
        return Collections.singletonList(result);
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
