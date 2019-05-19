package net.akami.mask.operation;

import net.akami.mask.affection.CalculationAffection;
import net.akami.mask.expression.Expression;
import net.akami.mask.handler.AffectionHandler;
import net.akami.mask.handler.BinaryOperationHandler;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext();

    private List<AffectionHandler> affectionHandlers = new ArrayList<>();
    private BinaryOperationHandler[] binaryHandlers;
    private MathContext bigDecimalContext = new MathContext(150);

    public MaskContext() {
        this.binaryHandlers = BinaryOperationHandler.generateDefaultHandlers(this);
    }

    public <T extends CalculationAffection> List<T> getAffections(Class<T> type) {
        List<T> list = new ArrayList<>();

        for(AffectionHandler handler : affectionHandlers) {
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
        for(AffectionHandler handler : affectionHandlers) {
            if(handler.getClass().equals(type))
                return (T) handler;
        }
        return null;
    }

    public <T> T binaryCompute(T a, T b, Class<? extends BinaryOperationHandler<T>> clazz) {
        BinaryOperationHandler<T> handler = getBinaryOperation(clazz);
        return handler.rawOperate(a, b);
    }

    public <T extends BinaryOperationHandler> T getBinaryOperation(Class<T> clazz) {

        for(BinaryOperationHandler current : binaryHandlers) {
            if(current.getClass().equals(clazz))
                return (T) current;
        }
        return null;
    }

    public void addHandler(AffectionHandler handler) {
        affectionHandlers.add(handler);
    }

    public MathContext getMathContext() {
        return bigDecimalContext;
    }
}
