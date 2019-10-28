package net.akami.atosym.merge.property.division;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.SubtractionMathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.operator.DivisionOperator;
import net.akami.atosym.operator.SubOperator;

import java.util.ArrayList;
import java.util.List;

public class DividedSubtractionProperty extends FairOverallMergeProperty<MathObject> {

    private MaskContext context;

    public DividedSubtractionProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2);
        this.context = context;
    }

    @Override
    protected MathObject computeResult() {
        SubtractionMathObject subObject = (SubtractionMathObject) p1;
        SubOperator subOperator = context.getOperator(SubOperator.class);

        List<MathObject> newObjects = new ArrayList<>(2);
        List<MathObject> children = subObject.getChildren();
        localDivision(newObjects, children, 0);
        localDivision(newObjects, children, 1);

        return subOperator.rawOperate(newObjects);
    }

    private void localDivision(List<MathObject> destination, List<MathObject> children, int index) {
        DivisionOperator divOperator = context.getOperator(DivisionOperator.class);
        destination.add(divOperator.binaryOperate(children.get(index), p2));
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.SUB;
    }
}
