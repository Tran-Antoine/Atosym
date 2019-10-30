package net.akami.atosym.merge.property.mult;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.ExponentMathObject;
import net.akami.atosym.merge.property.FairElementMergeProperty;

import java.util.Arrays;
import java.util.List;

public class VariableSquaredProperty extends FairElementMergeProperty<MathObject> {

    private MaskContext context;

    public VariableSquaredProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {
        List<MathObject> children = Arrays.asList(
                p1,
                new NumberExpression(2f)
        );
        constructed.add(new ExponentMathObject(children, context));
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.VARIABLE && p2.getType() == MathObjectType.VARIABLE
                && p1.equals(p2);
    }
}
