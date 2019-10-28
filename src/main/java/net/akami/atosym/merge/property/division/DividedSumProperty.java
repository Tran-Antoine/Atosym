package net.akami.atosym.merge.property.division;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.SumMathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.operator.DivOperator;
import net.akami.atosym.operator.SumOperator;

import java.util.ArrayList;
import java.util.List;

public class DividedSumProperty extends FairOverallMergeProperty<MathObject> {

    private MaskContext context;

    public DividedSumProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    protected MathObject computeResult() {
        SumMathObject sumObject = (SumMathObject) p1;
        DivOperator divOperator = context.getOperator(DivOperator.class);
        SumOperator sumOperator = context.getOperator(SumOperator.class);

        List<MathObject> newObjects = new ArrayList<>();

        for(MathObject monomial : sumObject.getChildren()) {
            newObjects.add(divOperator.binaryOperate(monomial, p2));
        }

        return sumOperator.sumMerge(newObjects);
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.SUM;
    }
}
