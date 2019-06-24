package net.akami.mask.alteration;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.handler.PowerCalculator;
import net.akami.mask.utils.ExpressionUtils;

public class PowExpansionLimit implements CalculationCanceller<Expression> {

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
        return context.getBinaryOperation(PowerCalculator.class).layerPow(input[0], input[1]);
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

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
