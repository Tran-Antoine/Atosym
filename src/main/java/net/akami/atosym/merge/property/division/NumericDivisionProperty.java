package net.akami.atosym.merge.property.division;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.global.NumericOperation;
import net.akami.atosym.merge.property.global.NumericOperationOverallProperty;
import net.akami.atosym.utils.NumericUtils;

public class NumericDivisionProperty extends NumericOperationOverallProperty {

    public NumericDivisionProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, context);
    }

    @Override
    protected NumericOperation function() {
        return NumericUtils::div;
    }
}
