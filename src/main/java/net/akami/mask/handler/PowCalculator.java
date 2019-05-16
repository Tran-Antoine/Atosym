package net.akami.mask.handler;

import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.math.BigDecimal;

public class PowCalculator extends BinaryOperationHandler<Expression> {

    public PowCalculator(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("PowCalculator operation process between {} and {} : \n", a, b);

        if(ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            float aFloat = ((Monomial) a.get(0)).getNumericValue();
            float bFloat = ((Monomial) b.get(0)).getNumericValue();
            return Expression.of((float) Math.pow(aFloat, bFloat));
        }

        return null;
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
