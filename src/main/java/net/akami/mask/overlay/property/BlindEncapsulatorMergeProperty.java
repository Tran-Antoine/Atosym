package net.akami.mask.overlay.property;

import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.expression.ComplexVariable;

public interface BlindEncapsulatorMergeProperty<T extends ExpressionOverlay> extends EncapsulatorMergeProperty {

    @Override
    default boolean isApplicableFor(ComplexVariable v1, ComplexVariable v2) {
        ExpressionOverlay last1 = v1.getOverlay(-1);
        ExpressionOverlay last2 = v2.getOverlay(-1);
        if(!(last1.getClass().equals(getSupportedClass()) && last2.getClass().equals(getSupportedClass())))
            return false;
        // Casts are secure
        return isApplicableFor((T) last1, (T) last2);
    }

    boolean isApplicableFor(T t1, T t2);
    Class<T> getSupportedClass();
}
