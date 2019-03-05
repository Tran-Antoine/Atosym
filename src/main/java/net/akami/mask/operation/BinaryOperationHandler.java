package net.akami.mask.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BinaryOperationHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperationHandler.class);
    protected final StringBuilder BUILDER = new StringBuilder();

    protected abstract String operate(String a, String b);
    public abstract String inFormat(String origin);
    public abstract String outFormat(String origin);

    public String rawOperate(String a, String b) {
        return outFormat(operate(inFormat(a), inFormat(b)));
    }

    public void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
