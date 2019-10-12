package net.akami.atosym.merge.property.power;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.global.NumericOperation;
import net.akami.atosym.merge.property.global.NumericOperationOverallProperty;
import net.akami.atosym.utils.NumericUtils;

public class NumericPowerProperty extends NumericOperationOverallProperty {

    public NumericPowerProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, context);
    }

    @Override
    protected NumericOperation function() {
        return NumericUtils::pow;
    }
}
