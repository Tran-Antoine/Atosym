package net.akami.mask.operation;

import net.akami.mask.operation.CalculationProperty.CalculationMode;

/**
 * Default mode, there is no operation needed, since Math.cos|sin|tan takes a value in radians.
 */
public class RadiansMode implements CalculationMode {

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
        return self;
    }
}
