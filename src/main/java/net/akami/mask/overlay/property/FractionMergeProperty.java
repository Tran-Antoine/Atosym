package net.akami.mask.overlay.property;

import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.FractionEncapsulator;
import net.akami.mask.expression.ComplexVariable;

public interface FractionMergeProperty extends EncapsulatorMergeProperty {

    @Override
    default boolean isApplicableFor(ComplexVariable v1, ComplexVariable v2) {
        ExpressionOverlay layer1 = v1.getLayer(-1);
        ExpressionOverlay layer2 = v2.getLayer(-1);

        if(!(layer1 instanceof FractionEncapsulator && layer2 instanceof FractionEncapsulator))
            return false;
        // equals is overridden in the expression class
        return isApplicableFor((FractionEncapsulator) layer1, (FractionEncapsulator) layer2);
    }

    /**
     * We are guaranteed that the last layer of the two var parts is a fraction
     * @param f1
     * @param f2
     * @return whether f1 and f2 are compatible or not
     */
    boolean isApplicableFor(FractionEncapsulator f1, FractionEncapsulator f2);
}
