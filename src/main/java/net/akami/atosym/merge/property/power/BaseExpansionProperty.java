package net.akami.atosym.merge.property.power;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.*;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.operator.MultOperator;

public class BaseExpansionProperty extends FairOverallMergeProperty<MathObject> {

    private MaskContext context;
    private float expansion;

    public BaseExpansionProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    protected MathObject computeResult() {
        MultOperator mult = context.getOperator(MultOperator.class);
        MathObject initial = MathObject.NEUTRAL_MULT;

        for(int i = 0; i < expansion; i++) {
            initial = mult.binaryOperate(initial, p1);
        }

        return initial;
    }

    @Override
    public boolean prepare() {
        if(p1.getType() == MathObjectType.SUM && p2.getType() == MathObjectType.NUMBER) {
            this.expansion = ((NumberExpression) p2).getValue();
            return expansion % 1 == 0;
        }
        return false;
    }
}
