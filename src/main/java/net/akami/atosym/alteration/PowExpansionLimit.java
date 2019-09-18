package net.akami.atosym.alteration;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.handler.PowerOperator;
import net.akami.atosym.utils.ExpressionUtils;

/**
 * Decides what the maximal numeric exponent is before canceling the power calculation. <br>
 * If set to 3 for instance, {@code (x+y)^3} will be expanded as {@code x^3 + 3x^2y + 3xy^2 + y^3},
 * whereas {@code (x+y)^4} will remain {@code (x+y)^4}
 */
public class PowExpansionLimit implements FairCalculationCanceller<Expression> {

    private int limit;
    private MaskContext context;

    public PowExpansionLimit(MaskContext context) {
        this(5, context);
    }

    public PowExpansionLimit(int limit, MaskContext context) {
        this.limit = limit;
        this.context = context;
    }

    @Override
    public Expression resultIfCancelled(Expression... input) {
        return context.getBinaryOperation(PowerOperator.class).layerPow(input[0], input[1]);
    }

    @Override
    public boolean appliesTo(Expression... input) {
        Expression exponent = input[1];
        // using parseFloat instead of parseInt for possible ".0", such as "5.0"
        return ExpressionUtils.isAnInteger(exponent) && Float.parseFloat(exponent.toString()) > limit;
    }

    @Override
    public float priorityLevel() {
        return 10;
    }

    /**
     * @param limit the maximal allowed numeric exponent before cancelling the calculation
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the maximal allowed numeric exponent before cancelling the calculation
     */
    public int getLimit() {
        return limit;
    }
}
