package net.akami.mask.handler;

import net.akami.mask.affection.CalculationCache;
import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.affection.MaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BinaryOperationHandler implements IODefaultFormatter, CancellableHandler, PostCalculationActionable {

    public static final BinaryOperationHandler[] DEFAULT_OPERATIONS = {
            new Adder(),
            new Subtractor(),
            new Multiplicator(),
            new Divider(),
            new PowCalculator()
    };

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
        postCalculation(a, b, result);
        return result;
    }

    public void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    @Override
    public void postCalculation(String a, String b, String result) {
        getAffection(CalculationCache.class).get().push(a+'|'+b, result);
    }

    @Override
    public CalculationCanceller[] getAffections() {
        return cancellers;
    }
}
