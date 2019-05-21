package net.akami.mask.overlay.property;

import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;

public interface EncapsulatorMergeProperty {

    boolean isApplicableFor(ComplexVariable v1, ComplexVariable v2);

    Expression merge(Expression a, Expression b);
}
