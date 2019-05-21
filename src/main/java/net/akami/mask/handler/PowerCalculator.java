package net.akami.mask.handler;

import net.akami.mask.overlay.ExponentEncapsulator;
import net.akami.mask.expression.*;
import net.akami.mask.core.MaskContext;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Collections;
import java.util.List;

public class PowerCalculator extends BinaryOperationHandler<Expression> {

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
        // Both casts are secured here.
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
        Expression denominator = extensiblePow(a, -b);

        // TODO return Expression.of(new SimpleFraction(numerator, denominator));
        return null;
    }

    private Expression layerPow(Expression a, Expression b) {
        List<Monomial> insights = a.getElements();
        ComplexVariable variable = new ComplexVariable(insights, Collections.singletonList(ExponentEncapsulator.fromExpression(b)));
        Expression newExpression = Expression.of(new Monomial(1, variable));
        return newExpression;
    }

    @Override
    public Expression inFormat(Expression origin) {
        return origin;
    }

    @Override
    public Expression outFormat(Expression origin) {
        return origin;
    }
}
