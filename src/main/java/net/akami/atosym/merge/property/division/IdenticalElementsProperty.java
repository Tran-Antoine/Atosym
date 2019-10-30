package net.akami.atosym.merge.property.division;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.BiElementMergeProperty;

import java.util.List;

public class IdenticalElementsProperty extends BiElementMergeProperty<MathObject> {

    public IdenticalElementsProperty(MathObject p1, MathObject p2) {
        super(p1, p2, false);
    }

    @Override
    public void blendResult(List<MathObject> listA, List<MathObject> listB) {
        // nothing to do, elements will automatically be nullified
    }

    @Override
    public boolean prepare() {
        return p1.equals(p2);
    }
}
