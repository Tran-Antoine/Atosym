package net.akami.mask.handler;

import net.akami.mask.affection.MaskContext;

public class TrigonometryCalculator extends BinaryOperationHandler {

    public TrigonometryCalculator(MaskContext context) {
        super(context);
    }

    @Override
    protected String operate(String a, String b) {
        return null;
    }

    @Override
    public String inFormat(String origin) {
        return origin;
    }

    @Override
    public String outFormat(String origin) {
        return null;
    }
}
