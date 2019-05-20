package net.akami.mask.encapsulator.property;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.expression.IrreducibleVarPart;

public interface BlindEncapsulatorMergeProperty<T extends ExpressionEncapsulator> extends EncapsulatorMergeProperty {

    @Override
    default boolean isApplicableFor(IrreducibleVarPart v1, IrreducibleVarPart v2) {
        ExpressionEncapsulator last1 = v1.getLayer(-1);
        ExpressionEncapsulator last2 = v2.getLayer(-1);
        if(!(last1.getClass().equals(getSupportedClass()) && last2.getClass().equals(getSupportedClass())))
            return false;
        // Casts are secure
        return isApplicableFor((T) last1, (T) last2);
    }

    boolean isApplicableFor(T t1, T t2);
    Class<T> getSupportedClass();
}
