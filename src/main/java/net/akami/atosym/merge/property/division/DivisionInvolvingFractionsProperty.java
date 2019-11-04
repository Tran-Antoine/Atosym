package net.akami.atosym.merge.property.division;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.merge.property.BiElementMergeProperty;
import net.akami.atosym.operator.BinaryOperator;
import net.akami.atosym.operator.MultOperator;

import java.util.ArrayList;
import java.util.List;

public class DivisionInvolvingFractionsProperty extends BiElementMergeProperty<MathObject> {

    private MaskContext context;

    public DivisionInvolvingFractionsProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, true);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> listA, List<MathObject> listB) {
        fill(p1, listA, listB);
        fill(p2, listB, listA);
    }

    private void fill(MathObject object, List<MathObject> from, List<MathObject> to) {
        BinaryOperator mult = context.getOperator(MultOperator.class);

        if(object.getType() != MathObjectType.DIV) {
            reorganize(object, from, mult);
            return;
        }
        FunctionObject function = (FunctionObject) object;
        reorganize(function.getChild(1), to, mult);
        reorganize(function.getChild(0), from, mult);
    }

    private void reorganize(MathObject target, List<MathObject> destination, BinaryOperator operator) {
        if(destination.size() == 0) {
            destination.add(target);
            return;
        }
        List<MathObject> elements = new ArrayList<>(2);
        elements.add(target);
        elements.addAll(destination);
        destination.clear();
        destination.add(operator.rawOperate(elements));
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.DIV || p2.getType() == MathObjectType.DIV;
    }
}
