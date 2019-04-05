package net.akami.mask.handler;

import net.akami.mask.operation.CalculationCache;
import net.akami.mask.operation.CalculationCanceller;
import net.akami.mask.operation.MaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BinaryOperation implements CancellableHandler, PostCalculationActionable {

    public static final BinaryOperation[] DEFAULT_OPERATIONS = {
            new Adder(),
            new Subtractor(),
            new Multiplicator(),
            new Divider(),
            new PowCalculator()
    };

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperation.class);
    protected final StringBuilder BUILDER = new StringBuilder();
    private char sign;
    protected MaskContext context;
    private CalculationCanceller[] cancellers;

    public BinaryOperation(char sign, MaskContext context) {
        this.sign = sign;
        this.context = context;
        this.cancellers = new CalculationCanceller[]{new CalculationCache()};
    }

    protected abstract String operate(String a, String b);
    public abstract String inFormat(String origin);
    public abstract String outFormat(String origin);

    public String rawOperate(String a, String b) {
        if(isCancellable(a, b)) {
            return result(a, b);
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
    public CalculationCanceller[] getCancellers() {
        return cancellers;
    }
}
