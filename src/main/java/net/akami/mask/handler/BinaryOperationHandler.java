package net.akami.mask.handler;

import net.akami.mask.affection.CalculationCache;
import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.encapsulator.MergePropertyManager;
import net.akami.mask.operation.MaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BinaryOperationHandler<T> implements IODefaultFormatter<T>, CancellableHandler<T>, PostCalculationActionable<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperationHandler.class);
    protected final StringBuilder BUILDER = new StringBuilder();
    protected MergePropertyManager propertyManager;
    protected MaskContext context;
    private CalculationCanceller[] cancellers;

    public BinaryOperationHandler(MaskContext context) {
        this.context = context;
        this.propertyManager = new MergePropertyManager(context);
        this.cancellers = new CalculationCanceller[]{/*new CalculationCache()*/};
    }

    protected abstract T operate(T a, T b);

    public T rawOperate(T a, T b) {
        if(isCancellable(a, b)) {
            return findResult(a, b);
        }
        T result = outFormat(operate(inFormat(a), inFormat(b)));
        postCalculation(result, a, b);
        return result;
    }

    public void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    @Override
    public void postCalculation(T result, T... input) {
        //getAffection(CalculationCache.class).get().push(input[0].toString()+'|'+input[1].toString(), result.toString());
    }

    @Override
    public CalculationCanceller[] getAffections() {
        return cancellers;
    }

    public static BinaryOperationHandler[] generateDefaultHandlers(MaskContext context) {
        return new BinaryOperationHandler[]{

                new Adder(context),
                new Subtractor(context),
                new Multiplier(context),
                new Divider(context),
                new PowCalculator(context)
        };
    }

    public MergePropertyManager getPropertyManager() {
        return propertyManager;
    }
}
