package net.akami.mask.handler;

import net.akami.mask.affection.CalculationCache;
import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.operation.MaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BinaryOperationHandler implements IODefaultFormatter, CancellableHandler, PostCalculationActionable {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperationHandler.class);
    protected final StringBuilder BUILDER = new StringBuilder();
    protected MaskContext context;
    private CalculationCanceller[] cancellers;

    public BinaryOperationHandler(MaskContext context) {
        this.context = context;
        this.cancellers = new CalculationCanceller[]{new CalculationCache()};
    }

    protected abstract String operate(String a, String b);

    public String rawOperate(String a, String b) {
        if(isCancellable(a, b)) {
            return findResult(a, b);
        }
        String result = outFormat(operate(inFormat(a), inFormat(b)));
        postCalculation(result, a, b);
        return result;
    }

    public void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    @Override
    public void postCalculation(String result, String... input) {
        getAffection(CalculationCache.class).get().push(input[0]+'|'+input[1], result);
    }

    @Override
    public CalculationCanceller[] getAffections() {
        return cancellers;
    }

    public static BinaryOperationHandler[] generateDefaultHandlers(MaskContext context) {
        return new BinaryOperationHandler[]{

                new Adder(context),
                new Subtractor(context),
                new Multiplicator(context),
                new Divider(context),
                new PowCalculator(context)
        };
    }
}
