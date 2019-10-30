package net.akami.atosym.merge.property.global;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.merge.property.FairElementMergeProperty;

import java.util.List;

public abstract class NumericOperationSequencedProperty extends FairElementMergeProperty<MathObject> {

    private MaskContext context;

    public NumericOperationSequencedProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    protected abstract NumericOperation function();

    @Override
    public void blendResult(List<MathObject> constructed) {
        NumberExpression numericA = (NumberExpression) p1;
        NumberExpression numericB = (NumberExpression) p2;

        constructed.add(new NumberExpression(numericA, numericB, function(), context));
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.NUMBER && p2.getType() == MathObjectType.NUMBER;
    }

}
