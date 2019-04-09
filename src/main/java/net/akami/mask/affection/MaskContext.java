package net.akami.mask.affection;

import net.akami.mask.handler.*;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The MaskContext object is one of the core objects of the Mask operator system. <p></p>
 * It handles
 * <ul>
 * <li> a list of {@link AffectionHandler}. Those handlers describe what affections are effective in this context.
 * <li> a MathContext, basically defining under what context {@link java.math.BigDecimal} calculations must be done.
 * </ul>
 *
 * In most of the cases a {@link MaskContext} is linked to a {@link net.akami.mask.operation.MaskHandler}, although
 * it might be useful alone for basic {@link net.akami.mask.utils.MathUtils} method calls, if a specific context needs
 * to be specified.
 *
 * @author Antoine Tran
 */
public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext(new MathContext(120));

    private List<AffectionHandler> handlers;
    private MathContext mathContext;

    public MaskContext(MathContext mathContext) {
        this.mathContext = mathContext;
        this.handlers = loadDefault();
    }

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
        for(CalculationAffection affection : getSingleHandler(hType).getAffections()) {
            if(affection.getClass().equals(type))
                return (T) affection;
        }
        return null;
    }

    public <T extends AffectionHandler> T getSingleHandler(Class<T> type) {
        for(AffectionHandler handler : handlers) {
            if(handler.getClass().equals(type))
                return (T) handler;
        }
        return null;
    }

    public <T extends AffectionHandler> List<T> getHandlers(Class<T> type) {
        List<T> handlers = new ArrayList<>();
        for(AffectionHandler handler : this.handlers) {
            if(handler.getAffections().equals(type))
                handlers.add((T) handler);
        }
        return handlers;
    }

    public void addHandler(AffectionHandler handler) {
        handlers.add(handler);
    }

    public List<BinaryOperationHandler> getOperations() {
        return getHandlers(BinaryOperationHandler.class);
    }

    public <T extends BinaryOperationHandler> T getOperation(Class<T> type) {
        for(AffectionHandler handler : this.handlers) {
            if(handler.getClass().equals(type))
                return (T) handler;
        }
        return null;
    }

    public MathContext getMathContext() {
        return mathContext;
    }

    public static List<AffectionHandler> loadDefault() {
        return new ArrayList<>(Arrays.asList(
                new Adder(), new Subtractor(),
                new Multiplicator(), new Divider(),
                new PowCalculator()
        ));
    }
}
