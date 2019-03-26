package net.akami.mask.operation;

import net.akami.mask.operation.CalculationProperty.CalculationMode;
import net.akami.mask.utils.ExpressionUtils;

public class DegreesMode implements CalculationMode {

    @Override
    public boolean cancelsOperation(String self) {
        return false;
    }

    @Override
    public CalculationProperty getAttachment() {
        return CalculationProperty.ANGLES_UNIT;
    }

    @Override
    public String[] compute(String... self) {
        if(!ExpressionUtils.isANumber(self[0]))
            return self;

        self[0] = String.valueOf(Math.toRadians(Double.parseDouble(self[0])));
        return self;
    }
}
