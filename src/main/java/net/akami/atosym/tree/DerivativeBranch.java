package net.akami.atosym.tree;

import net.akami.atosym.expression.MathObject;

/**
 * Implementation of the base Branch class. The {@link DerivativeBranch} class brings two changes :
 * <br>
 * - It adds a new value, the derivative value <br>
 * - It redefines the {@code canBeEvaluated} method, making it return true all the time
 *
 * @author Antoine Tran
 */
public class DerivativeBranch extends SimpleBranch {

    private MathObject derivativeValue;

    public DerivativeBranch(String expression) {
        super(null, expression, null);
    }

    /**
     * A derivative branch can always be evaluated. If it does not have children, we can evaluate the
     * derivative of it.
     * @return {@code true}
     */
    public boolean canBeEvaluated() {
        return true;
    }

    public MathObject getDerivativeValue() {
        return derivativeValue;
    }

    public void setDerivativeValue(MathObject derivativeValue) {
        this.derivativeValue = derivativeValue;
    }
}
