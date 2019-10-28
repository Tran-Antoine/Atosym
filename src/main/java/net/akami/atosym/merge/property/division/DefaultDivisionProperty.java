package net.akami.atosym.merge.property.division;

import net.akami.atosym.expression.DivisionMathObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;

import java.util.Arrays;

public class DefaultDivisionProperty extends FairOverallMergeProperty<MathObject> {

    public DefaultDivisionProperty(MathObject p1, MathObject p2) {
        super(p1, p2);
    }

    @Override
    protected MathObject computeResult() {
        return new DivisionMathObject(Arrays.asList(p1, p2));
    }

    @Override
    public boolean prepare() {
        return true;
    }
}
