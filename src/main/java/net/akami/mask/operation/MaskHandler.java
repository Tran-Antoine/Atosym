package net.akami.mask.operation;

import net.akami.mask.affection.MaskContext;
import net.akami.mask.handler.BinaryOperationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class MaskHandler {

    private MaskExpression current;
    private MaskContext context;
    private List<MaskOperator> operators;
    private boolean setToOut = true;

    public MaskHandler() {
        this.context = MaskContext.DEFAULT;
        this.operators = MaskOperator.defaultOperators();
    }

    public void begin(MaskExpression current) {
        this.current = current;
    }

    public void setCurrentToOut(boolean setToOut) {
        this.setToOut = setToOut;
    }

    public <E, T extends MaskOperator<E>> MaskHandler compute(Class<T> op, MaskExpression out, E extraData) {
        return compute(op, current, out, extraData, null);
    }

    public <E, T extends MaskOperator<E>> MaskHandler compute(Class<T> op, MaskExpression in, MaskExpression out, E extraData) {
        return compute(op, in, out, extraData,null);
    }

    public <E, T extends MaskOperator<E>> MaskHandler compute(Class<T> op, MaskExpression out,
                                                        E extraData, Consumer<MaskExpression> outAction) {
        return compute(op, current, out, extraData, outAction);
    }

    public <E, T extends MaskOperator<E>> MaskHandler compute(Class<T> op, MaskExpression in, MaskExpression out,
                                                        E extraData, Consumer<MaskExpression> outAction) {
        Objects.requireNonNull(in);

        if(out == null)
            out = MaskExpression.TEMP;

        MaskOperator operator = findByType(op);
        operator.compute(in, out, extraData, this.context);
        if(outAction != null) {
            outAction.accept(out);
        }

        if(setToOut)
            this.current = out;

        return this;
    }

    private MaskOperator findByType(Class<? extends MaskOperator> type) {
        for(MaskOperator operator : operators) {
            if(operator.getClass().equals(type))
                return operator;
        }
        return null;
    }

    public String asExpression() {
        Objects.requireNonNull(current);
        return current.getExpression();
    }

    public void end() {
        this.current = null;
    }

    public MaskContext getContext() {
        return context;
    }
}
