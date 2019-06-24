package net.akami.mask.handler;

import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.expression.*;
import net.akami.mask.core.MaskContext;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Collections;
import java.util.List;

public class PowerCalculator extends BinaryOperationHandler {

    public PowerCalculator(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("PowerCalculator operation process between {} and {} : \n", a, b);

        if(ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            return fullNumericPow(a, b);
        }

        if(ExpressionUtils.isAnInteger(b)) {
            Monomial first = b.get(0);
            return extensiblePow(a, (int) first.getNumericValue());
        }

        return layerPow(a, b);
    }

    private Expression fullNumericPow(Expression a, Expression b) {
        float aFloat = a.get(0).getNumericValue();
        float bFloat = b.get(0).getNumericValue();
        return Expression.of((float) Math.pow(aFloat, bFloat));
    }

    private Expression extensiblePow(Expression a, int val) {

        if(val == 0) return Expression.of(1);
        if(val < 0) return negativeExtensiblePow(a, val);

        Expression finalExpression = (Expression) a.clone();

        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);
        for(int i = 1; i < val; i++) {
            finalExpression = multiplier.operate(finalExpression, a);
        }
        return finalExpression;
    }

    private Expression negativeExtensiblePow(Expression a, int b) {

        Monomial numerator = new NumberElement(1);
        // b is negative, we must convert it to a positive number
        Expression denominator = extensiblePow(a, -b);

        throw new RuntimeException("Unsupported yet");
    }

    // The entire expression becomes a monomial with 1 as numeric value, encapsulated in a new overlay
    public Expression layerPow(Expression a, Expression b) {
        List<Monomial> insights = a.getElements();
        IntricateVariable variable = new IntricateVariable(insights, Collections.singletonList(ExponentOverlay.fromExpression(b)));
        Expression newExpression = Expression.of(new Monomial(1, variable));
        return newExpression;
    }
}
