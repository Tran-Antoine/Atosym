package net.akami.atosym.merge.property.power;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.ExponentMathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;

import java.util.Arrays;

public class DefaultPowerProperty extends FairOverallMergeProperty<MathObject> {

    private MaskContext context;

    public DefaultPowerProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2);
        this.context = context;
    }

    @Override
    protected MathObject computeResult() {
        return new ExponentMathObject(Arrays.asList(p1, p2), context);
    }

    @Override
    public boolean prepare() {
        return true;
    }
}
