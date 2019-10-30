package net.akami.atosym.merge.property.division;

import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.merge.property.BiElementMergeProperty;

import java.util.List;

public class DivisionOfMultiplicationProperty extends BiElementMergeProperty<MathObject> {

    public DivisionOfMultiplicationProperty(MathObject p1, MathObject p2) {
        super(p1, p2, true);
    }

    @Override
    public void blendResult(List<MathObject> listA, List<MathObject> listB) {
        blendResult(p1, listA);
        blendResult(p2, listB);
    }

    private void blendResult(MathObject target, List<MathObject> list) {
        if(target.getType() == MathObjectType.MULT) {
            list.addAll(((FunctionObject) target).getChildren());
            return;
        }
        list.add(target);
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.MULT || p2.getType() == MathObjectType.MULT;
    }
}
