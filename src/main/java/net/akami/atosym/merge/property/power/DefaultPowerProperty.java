package net.akami.atosym.merge.property.power;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.PowerMathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;

import java.util.Arrays;

public class DefaultPowerProperty extends FairOverallMergeProperty<MathObject> {

    public DefaultPowerProperty(MathObject p1, MathObject p2) {
        super(p1, p2);
    }

    @Override
    protected MathObject computeResult() {
        return new PowerMathObject(Arrays.asList(p1, p2));
    }

    @Override
    public boolean prepare() {
        return true;
    }
}
