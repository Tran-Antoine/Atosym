package net.akami.atosym.merge.property.sum;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.global.NumericOperation;
import net.akami.atosym.merge.property.global.NumericOperationSequencedProperty;
import net.akami.atosym.utils.NumericUtils;

public class NumericSumProperty extends NumericOperationSequencedProperty {

    public NumericSumProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, context);
    }

    @Override
    protected NumericOperation function() {
        return NumericUtils::sum;
    }
}
