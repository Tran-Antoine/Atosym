package net.akami.mask.operation;

import net.akami.mask.handler.AffectionHandler;

import java.util.ArrayList;
import java.util.List;

public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext();
    private List<AffectionHandler> handlers = new ArrayList<>();

    public <T extends CalculationAffection> List<T> getAffections(Class<T> type) {
        List<T> list = new ArrayList<>();

        for(AffectionHandler handler : handlers) {
            for(CalculationAffection affection : handler.getAffections()) {
                if(affection.getClass().equals(type))
                    list.add((T) affection);
            }
        }
        return list;
    }

    public <T extends CalculationAffection> T getAffection(Class<T> type, Class<? extends AffectionHandler> hType) {
        for(CalculationAffection affection : getHandler(hType).getAffections()) {
            if(affection.getClass().equals(type))
                return (T) affection;
        }
        return null;
    }

    public <T extends AffectionHandler> T getHandler(Class<T> type) {
        for(AffectionHandler handler : handlers) {
            if(handler.getClass().equals(type))
                return (T) handler;
        }
        return null;
    }

    public void addHandler(AffectionHandler handler) {
        handlers.add(handler);
    }
}
