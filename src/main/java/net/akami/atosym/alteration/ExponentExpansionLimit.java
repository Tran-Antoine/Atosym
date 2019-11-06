package net.akami.atosym.alteration;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.ExponentMathObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.NumericUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Decides what the maximal numeric exponent is before canceling the power calculation. <br>
 * If set to 3 for instance, {@code (x+y)^3} will be expanded as {@code x^3 + 3x^2y + 3xy^2 + y^3},
 * whereas {@code (x+y)^4} will remain {@code (x+y)^4}
 */
public class ExponentExpansionLimit implements FairCalculationCanceller<MathObject> {

    private int limit;
    private MaskContext context;

    public ExponentExpansionLimit(MaskContext context) {
        this(5, context);
    }

    public ExponentExpansionLimit(int limit, MaskContext context) {
        this.limit = limit;
        this.context = context;
    }

    @Override
    public MathObject resultIfCancelled(List<MathObject> input) {
        return new ExponentMathObject(Arrays.asList(input.get(0), input.get(1)), context);
    }

    @Override
    public boolean appliesTo(List<MathObject> input) {
        MathObject exponent = input.get(1);
        // using parseFloat instead of parseInt for possible ".0", such as "5.0"
        return NumericUtils.isAnInteger(exponent) && Float.parseFloat(exponent.toString()) > limit;
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
