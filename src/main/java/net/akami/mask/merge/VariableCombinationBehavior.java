package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.handler.Adder;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines whether two variables can be combined into a single variable
 */
public class VariableCombinationBehavior implements MergeBehavior<Variable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VariableCombinationBehavior.class);
    private MaskContext context;

    public VariableCombinationBehavior(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isMergeable(Variable a, Variable b) {

        List<ExpressionOverlay> aOverlays = a.getOverlaysSection(0, -2);
        List<ExpressionOverlay> bOverlays = b.getOverlaysSection(0, -2);

        return a.elementsEqual(b) && aOverlays.equals(bOverlays);
    }

    @Override
    public MergeResult<Variable> mergeElement(Variable a, Variable b) {

        Adder operator = context.getBinaryOperation(Adder.class);
        Expression exponentA = (Expression) a.getOverlay(-1);
        Expression exponentB = (Expression) b.getOverlay(-1);
        ExponentOverlay finalExp = ExponentOverlay.fromExpression(operator.operate(exponentA, exponentB));
        List<ExpressionOverlay> newOverlays = new ArrayList<>(a.getOverlaysSection(0, -2));
        newOverlays.add(finalExp);
        return new MergeResult<>(new ComplexVariable(b.getElements(), newOverlays), false);
    }
    
    @Override
    public Class<? extends Variable> getHandledType() {
        return Variable.class;
    }
}
