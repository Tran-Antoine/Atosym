package net.akami.mask.operation;

import net.akami.mask.handler.BinaryOperationHandler;

import java.util.HashMap;
import java.util.Map;

public class MaskOperator {

    private MaskContext context;
    private Map<String, String> cache = new HashMap<>();
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

    public void addToCache(String initial, String result) {
        CalculationProperty property = context.getProperty(CalculationProperty.CACHE_SIZE);
        int maxSize = ((CacheSizeProperty) property.getCurrentMode()).getSize();

        if(cache.size() < maxSize)
            cache.put(initial, result);
    }

    public MaskContext getContext() {
        return context;
    }
}
