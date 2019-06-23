package net.akami.mask.handler;

import net.akami.mask.alteration.CalculationCache;
import net.akami.mask.alteration.CalculationCanceller;
import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class BinaryOperationHandler implements CancellableHandler<Expression>, PostCalculationActionable<Expression> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperationHandler.class);
    protected MaskContext context;
    private List<CalculationCanceller<Expression>> cancellers;

    public BinaryOperationHandler(MaskContext context) {
        this.context = context;
        this.cancellers = new ArrayList<>();
    }

    protected abstract Expression operate(Expression a, Expression b);

    public Expression rawOperate(Expression a, Expression b) {
        if(isCancellable(a, b)) {
            return findResult(a, b);
        }
        Expression result = operate(a, b);
        postCalculation(result, a, b);
        return result;
    }

    @Override
    public void postCalculation(Expression result, Expression... input) {
        String toPush = input[0] + "|" + input[1];
        getAffection(CalculationCache.class).ifPresent(affection -> affection.push(toPush, result));
    }

    @Override
    public List<CalculationCanceller<Expression>> getAffections() {
        return cancellers;
    }

    public static Set<BinaryOperationHandler> generateDefaultHandlers(MaskContext context) {
        return new HashSet<>(Arrays.asList(
                new Adder(context),
                new Subtractor(context),
                new Multiplier(context),
                new Divider(context),
                new PowerCalculator(context)
        ));
    }
}
