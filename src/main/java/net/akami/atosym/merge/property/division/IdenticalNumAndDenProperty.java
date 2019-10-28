package net.akami.atosym.merge.property.division;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;

public class IdenticalNumAndDenProperty extends FairOverallMergeProperty<MathObject> {

    public IdenticalNumAndDenProperty(MathObject p1, MathObject p2) {
        super(p1, p2);
    }

    @Override
    protected MathObject computeResult() {
        return MathObject.NEUTRAL_DIV;
    }

    @Override
    public boolean prepare() {
        return p1.equals(p2);
    }
}
