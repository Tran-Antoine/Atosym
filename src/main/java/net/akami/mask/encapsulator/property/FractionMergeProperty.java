package net.akami.mask.encapsulator.property;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.encapsulator.FractionEncapsulator;
import net.akami.mask.expression.IrreducibleVarPart;

public interface FractionMergeProperty extends EncapsulatorMergeProperty {

    @Override
    default boolean isApplicableFor(IrreducibleVarPart v1, IrreducibleVarPart v2) {
        ExpressionEncapsulator layer1 = v1.getLayer(-1);
        ExpressionEncapsulator layer2 = v2.getLayer(-1);

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
