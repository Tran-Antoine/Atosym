package net.akami.mask.operation;

import net.akami.mask.handler.AffectionHandler;

import java.util.List;

public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext();
    private List<AffectionHandler> handlers;

    public <T extends CalculationAffection> T getAffection(Class<T> type) {
        for(AffectionHandler affection : handlers) {
            if(affection.getClass().equals(type))
                return (T) affection;
        }
        return null;
    }
}
