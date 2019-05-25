package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.handler.Adder;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        if (a instanceof SingleCharVariable && b instanceof SingleCharVariable) {
            return ((SingleCharVariable) a).getVar() == ((SingleCharVariable) b).getVar();
        }

        if(a instanceof SingleCharVariable) {
            return simpleCompatibleWithComplex((SingleCharVariable) a, (ComplexVariable) b);
        }

        if(b instanceof SingleCharVariable) {
            return simpleCompatibleWithComplex((SingleCharVariable) b, (ComplexVariable) a);
        }

        ComplexVariable complexA = (ComplexVariable) a;
        ComplexVariable complexB = (ComplexVariable) b;
        List<ExpressionOverlay> aOverlays = complexA.getOverlaysSection(0, -1);
        List<ExpressionOverlay> bOverlays = complexB.getOverlaysSection(0, -1);

        return complexA.elementsEqual(complexB) && aOverlays.equals(bOverlays);
    }

    private boolean simpleCompatibleWithComplex(SingleCharVariable a, ComplexVariable b) {
        if (b.getElementsSize() != 1 || !(b.getOverlay(-1) instanceof ExponentOverlay))
            return false;

        Monomial first = b.getElement(0);
        if (b.getElementsSize() != 1 || !first.getVarPart().isSimple())
            return false;

        SingleCharVariable firstSingle = (SingleCharVariable) first.getVarPart().get(0);
        return a.getVar() == firstSingle.getVar();
    }

    @Override
    public MergeResult<Variable> mergeElement(Variable a, Variable b) {
        if(a instanceof SingleCharVariable && b instanceof SingleCharVariable) {
            ExpressionOverlay exponent = ExponentOverlay.SQUARED;
            Monomial single = new Monomial(((SingleCharVariable) a).getVar(), context);
            return new MergeResult<>(new ComplexVariable(single, exponent), false);
        }

        Adder operator = context.getBinaryOperation(Adder.class);

        if(a instanceof SingleCharVariable) {
            return mergeSingleWithComplex((SingleCharVariable) a, (ComplexVariable) b, operator);
        }

        if(b instanceof SingleCharVariable) {
            return mergeSingleWithComplex((SingleCharVariable) b, (ComplexVariable) a, operator);
        }

        ComplexVariable compA = (ComplexVariable) a;
        ComplexVariable compB = (ComplexVariable) b;
        Expression exponentA = (Expression) compA.getOverlay(-1);
        Expression exponentB = (Expression) compB.getOverlay(-1);
        ExponentOverlay finalExp = ExponentOverlay.fromExpression(operator.operate(exponentA, exponentB));
        List<ExpressionOverlay> newOverlays = compA.getOverlaysSection(0, -1);
        newOverlays.add(finalExp);
        return new MergeResult<>(new ComplexVariable(compA.getElements(), newOverlays), false);
    }

    private MergeResult<Variable> mergeSingleWithComplex(SingleCharVariable a, ComplexVariable b, Adder operator) {
        Monomial single = new Monomial(a.getVar(), context);
        ExponentOverlay newExponent = ExponentOverlay.fromExpression(
                operator.operate((Expression) b.getOverlay(-1), Expression.of(1)));
        return new MergeResult<>(new ComplexVariable(single, newExponent), false);
    }
    
    @Override
    public Class<? extends Variable> getHandledType() {
        return Variable.class;
    }
}
