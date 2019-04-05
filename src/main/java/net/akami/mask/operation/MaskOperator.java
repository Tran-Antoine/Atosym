package net.akami.mask.operation;

import net.akami.mask.handler.BinaryOperation;

public class MaskOperator {

    private MaskContext context;
    private BinaryOperation[] supportedOperations;

    public MaskOperator() {
        this.context = MaskContext.DEFAULT;
        this.supportedOperations = BinaryOperation.DEFAULT_OPERATIONS;
    }

    public <T extends BinaryOperation> T getHandler(Class<T> type) {
        for(BinaryOperation handler : supportedOperations) {
            if(handler.getClass().equals(type)) return (T) handler;
        }
        return null;
    }

    public MaskContext getContext() {
        return context;
    }
}
