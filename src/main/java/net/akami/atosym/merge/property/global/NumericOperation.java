package net.akami.atosym.merge.property.global;

import net.akami.atosym.core.MaskContext;

@FunctionalInterface
public interface NumericOperation {
    float compute(float a, float b, MaskContext context);
}
