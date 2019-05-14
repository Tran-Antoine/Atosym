package net.akami.mask.handler;

import net.akami.mask.expression.Expression;
import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.util.List;

public class Subtractor extends BinaryOperationHandler<Expression> {

    public Subtractor(MaskContext context) {
        super(context);
    }

    @Override
    protected Expression operate(Expression a, Expression b) {
        LOGGER.info("Subtractor process of {} |-| {}: \n", a, b);

        List<String> monomials = null;//ExpressionUtils.toMonomials(a);
        List<String> bMonomials = null;//ExpressionUtils.toMonomials(b);

        // Changes the sign of the monomials that need to be subtracted
        for (int i = 0; i < bMonomials.size(); i++) {
            String m = bMonomials.get(i);

            if (m.startsWith("+")) {
                bMonomials.set(i, "-" + m.substring(1));
            } else if (m.startsWith("-")) {
                bMonomials.set(i, "+" + m.substring(1));
            } else {
                bMonomials.set(i, "-" + m);
            }
        }
        monomials.addAll(bMonomials);
        return null;//MathUtils.sum(monomials, context);
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
