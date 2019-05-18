package net.akami.mask.handler;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.expression.*;
import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PowCalculator extends BinaryOperationHandler<Expression> {

    public PowCalculator(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("PowCalculator operation process between {} and {} : \n", a, b);

        if(ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            return fullNumericPow(a, b);
        }

        if(ExpressionUtils.isAnInteger(b)) {
            Monomial first = (Monomial) b.get(0);
            return extensiblePow(a, (int) first.getNumericValue());
        }

        return layerPow(a, b);
    }

    private Expression fullNumericPow(Expression a, Expression b) {
        float aFloat = ((Monomial) a.get(0)).getNumericValue();
        float bFloat = ((Monomial) b.get(0)).getNumericValue();
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

        Monomial numerator = new Monomial(1);
        Expression denominator = extensiblePow(a, -b);

        return Expression.of(new SimpleFraction(numerator, denominator));
    }

    private Expression layerPow(Expression a, Expression b) {
        List<ExpressionElement> insights = a.getElements();
        ComposedVariable variable = new ComposedVariable(insights, Collections.singletonList(b));
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
