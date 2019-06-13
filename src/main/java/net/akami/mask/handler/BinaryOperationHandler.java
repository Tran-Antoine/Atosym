package net.akami.mask.handler;

import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class BinaryOperationHandler<T> implements CancellableHandler<T>, PostCalculationActionable<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperationHandler.class);
    protected MaskContext context;
    private List<CalculationCanceller<T>> cancellers;

    public BinaryOperationHandler(MaskContext context) {
        this.context = context;
        this.cancellers = new ArrayList<>();
    }

    protected abstract T operate(T a, T b);

    public T rawOperate(T a, T b) {
        if(isCancellable(a, b)) {
            return findResult(a, b);
        }
        T result = operate(a, b);
        postCalculation(result, a, b);
        return result;
    }

    @Override
    public void postCalculation(T result, T... input) {
        //getAffection(CalculationCache.class).getElement().push(input[0].toString()+'|'+input[1].toString(), merge.toString());
    }

    @Override
    public List<CalculationCanceller<T>> getAffections() {
        return cancellers;
    }

    public static Set<BinaryOperationHandler<Expression>> generateDefaultHandlers(MaskContext context) {
        return new HashSet<>(Arrays.asList(
                new Adder(context),
                new Subtractor(context),
                new Multiplier(context),
                new Divider(context),
                new PowerCalculator(context)
        ));
    }
}
