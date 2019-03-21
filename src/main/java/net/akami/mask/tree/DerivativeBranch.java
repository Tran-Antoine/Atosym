package net.akami.mask.tree;

public class DerivativeBranch extends Branch<DerivativeBranch> {

    private String derivativeValue;

    public DerivativeBranch(String expression) {
        super(expression);
    }

    /**
     * A derivative branch can always be evaluated. If it does not have children, we can evaluate the
     * derivatives of it.
     * @return
     */
    @Override
    public boolean canBeEvaluated() {
        return true;
    }

    public String getDerivativeValue() {
        return derivativeValue;
    }

    public void setDerivativeValue(String derivativeValue) {
        this.derivativeValue = derivativeValue;
    }
}
