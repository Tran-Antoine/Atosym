package net.akami.atosym.merge.property.mult;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.PowerMathObject;
import net.akami.atosym.merge.property.SimpleElementMergeProperty;

import java.util.Arrays;
import java.util.List;

public class VariableSquaredProperty extends SimpleElementMergeProperty<MathObject> {

    public VariableSquaredProperty(MathObject p1, MathObject p2) {
        super(p1, p2, false);
    }

    @Override
    public void blendResult(List<MathObject> constructed) {
        List<MathObject> children = Arrays.asList(
                p1,
                new NumberExpression(2f)
        );
        constructed.add(new PowerMathObject(children));
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.VARIABLE && p2.getType() == MathObjectType.VARIABLE
                && p1.equals(p2);
    }
}
