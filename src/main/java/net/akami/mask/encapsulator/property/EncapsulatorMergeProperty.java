package net.akami.mask.encapsulator.property;

import net.akami.mask.expression.Expression;
import net.akami.mask.expression.IrreducibleVarPart;

public interface EncapsulatorMergeProperty {

    boolean isApplicableFor(IrreducibleVarPart v1, IrreducibleVarPart v2);

    Expression merge(Expression a, Expression b);
}
