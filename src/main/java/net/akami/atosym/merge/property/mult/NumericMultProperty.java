package net.akami.atosym.merge.property.mult;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.global.NumericOperation;
import net.akami.atosym.merge.property.global.NumericOperationSequencedProperty;
import net.akami.atosym.utils.NumericUtils;

public class NumericMultProperty extends NumericOperationSequencedProperty {

    public NumericMultProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, context);
    }

    @Override
    protected NumericOperation function() {
        return NumericUtils::mult;
    }
}
