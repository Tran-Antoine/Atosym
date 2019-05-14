package net.akami.mask.expression;

import net.akami.mask.utils.Mergeable;

public interface ExpressionElement<T extends ExpressionElement> extends Mergeable<T> {

    String getExpression();
}
