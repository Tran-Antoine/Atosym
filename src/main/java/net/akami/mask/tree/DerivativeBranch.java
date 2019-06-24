package net.akami.mask.tree;

import net.akami.mask.expression.Expression;

/**
 * Implementation of the base Branch class. The {@link DerivativeBranch} class brings two changes :
 * <br>
 * - It adds a new value, the derivative value <br>
 * - It redefines the {@code canBeEvaluated} method, making it return true all the time
 *
 * @author Antoine Tran
 */
public class DerivativeBranch extends Branch<DerivativeBranch> {

    private Expression derivativeValue;

    public DerivativeBranch(String expression) {
        super(expression);
    }

    /**
     * A derivative branch can always be evaluated. If it does not have children, we can evaluate the
     * derivative of it.
     * @return {@code true}
     */
    @Override
    public boolean canBeEvaluated() {
        return true;
    }

    public Expression getDerivativeValue() {
        return derivativeValue;
    }

    public void setDerivativeValue(Expression derivativeValue) {
        this.derivativeValue = derivativeValue;
    }
}
