package net.akami.mask.operation;

import net.akami.mask.affection.MaskContext;
import net.akami.mask.handler.BinaryOperationHandler;

public class MaskOperator {

    private MaskContext context;
    private BinaryOperationHandler[] supportedOperations;

    public MaskOperator() {
        this.context = MaskContext.DEFAULT;
        this.supportedOperations = BinaryOperationHandler.DEFAULT_OPERATIONS;
    }

    public <T extends BinaryOperationHandler> T getHandler(Class<T> type) {
        for(BinaryOperationHandler handler : supportedOperations) {
            if(handler.getClass().equals(type)) return (T) handler;
        }
        return null;
    }

    public MaskContext getContext() {
        return context;
    }
}
