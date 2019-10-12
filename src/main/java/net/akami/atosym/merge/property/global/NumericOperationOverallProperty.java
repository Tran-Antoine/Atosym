package net.akami.atosym.merge.property.global;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.merge.property.FairOverallMergeProperty;

public abstract class NumericOperationOverallProperty extends FairOverallMergeProperty<MathObject> {

    private MaskContext context;

    public NumericOperationOverallProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    protected abstract NumericOperation function();

    @Override
    protected MathObject computeResult() {
        NumberExpression exp1 = (NumberExpression) p1;
        NumberExpression exp2 = (NumberExpression) p2;
        return new NumberExpression(exp1, exp2, function(), context);
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.NUMBER && p2.getType() == MathObjectType.NUMBER;
    }
}
