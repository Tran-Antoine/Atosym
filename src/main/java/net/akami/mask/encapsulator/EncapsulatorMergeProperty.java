package net.akami.mask.encapsulator;

import net.akami.mask.expression.ComposedVariable;
import net.akami.mask.expression.ExpressionElement;

import java.util.List;

public interface EncapsulatorMergeProperty {

    boolean isApplicableFor(List<ExpressionEncapsulator> l1, List<ExpressionEncapsulator> l2);

    ComposedVariable result(List<ExpressionEncapsulator> l1, List<ExpressionEncapsulator> l2, List<ExpressionElement> insights);
}
