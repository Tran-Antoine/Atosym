package net.akami.mask.function;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.IntricateVariable;
import net.akami.mask.expression.Monomial;
import net.akami.mask.function.TrigonometryOperation.UnaryOperation;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.utils.ExpressionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public abstract class NumberRequiredFunction extends MathFunction<Expression> {

    public NumberRequiredFunction(char binding, String name, MaskContext context) {
        super(binding, name, context, 1);
    }

    @Override
    protected Expression operate(Expression... input) {
        Expression unique = input[0];
        if(!ExpressionUtils.isANumber(unique)) return encapsulate(unique);
        else return compute(unique);
    }

    private Expression encapsulate(Expression unique) {
        List<Monomial> elements = unique.getElements();
        List<ExpressionOverlay> overlays = Collections.singletonList(this);

        IntricateVariable var = new IntricateVariable(elements, overlays);
        return Expression.of(new Monomial(1, var));
    }

    private Expression compute(Expression unique) {
        double result = function().compute(Double.parseDouble(unique.toString()));
        BigDecimal decimal = new BigDecimal(result).setScale(6, BigDecimal.ROUND_HALF_UP);
        return Expression.of(decimal.floatValue());
    }

    protected abstract UnaryOperation function();
}
