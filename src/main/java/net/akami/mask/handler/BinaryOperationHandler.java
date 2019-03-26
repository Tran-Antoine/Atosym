package net.akami.mask.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BinaryOperationHandler {

    public static final BinaryOperationHandler[] DEFAULT_OPERATIONS = {
            new Adder(),
            new Subtractor(),
            new Multiplicator(),
            new Divider(),
            new PowCalculator()
    };

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
