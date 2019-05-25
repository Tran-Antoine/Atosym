package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.PairNullifying;
import net.akami.mask.merge.VariableCombinationBehavior;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.VariableComparator;
import net.akami.mask.utils.VariableUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Divider extends BinaryOperationHandler<Expression> {

    public Divider(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("Divider process of {} |/| {}: \n", a, b);

        if(b.length() == 1 && b.get(0) != null && b.get(0).getNumericValue() == 0)
            throw new IllegalArgumentException("Could not compute a division by zero");

        // Avoids division by zero error after simplifying all the elements.
        // Also avoids useless calculations
        if(a.equals(b))
            return Expression.of(1);

        if (ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            // We are guaranteed that both expression have only one element, which is a monomial
            return Expression.of(numericalDivision(a.get(0), b.get(0)));
        }

        return algebraicDivision(a, b);
    }

    private Expression algebraicDivision(Expression a, Expression b) {
        if(b.length() > 1) {
            LOGGER.warn("Unable to calculate the division, the denominator being a polynomial. Returns a/b");
            return uncompletedDivision(a, b);
        }

        List<Monomial> finalElements = new ArrayList<>(a.length());

        for(Monomial numPart : a.getElements()) {
            finalElements.add(simpleDivision(numPart, b.get(0)));
        }

        Adder adder = context.getBinaryOperation(Adder.class);
        return adder.monomialSum(finalElements);
    }

    public NumberElement numericalDivision(Monomial a, Monomial b) {
        float result = floatDivision(a.getNumericValue(), b.getNumericValue());
        LOGGER.info("Numeric division. Result of {} / {} : {}", a, b, result);
        return new NumberElement(result);
    }

    public float floatDivision(float a, float b) {
        BigDecimal bigA = new BigDecimal(a);
        BigDecimal bigB = new BigDecimal(b);
        return bigA.divide(bigB, context.getMathContext()).floatValue();
    }

    private Expression uncompletedDivision(Expression a, Expression b) {
        FractionOverlay overlay = FractionOverlay.fromExpression(b);
        ComplexVariable result = new ComplexVariable(a.getElements(), overlay);
        return Expression.of(new Monomial(1, result));
    }

    public Monomial simpleDivision(Monomial a, Monomial b) {
        List<Variable> numVariables = new ArrayList<>(a.getVarPart().getVariables()); // creates copies
        List<Variable> denVariables = new ArrayList<>(b.getVarPart().getVariables());
        float aFloat = a.getNumericValue();
        float bFloat = b.getNumericValue();
        List<Float> aFloatList = MathUtils.decomposeNumber(aFloat, bFloat);
        List<Float> bFloatList = MathUtils.decomposeNumber(bFloat, aFloat);

        switchFractionsLocation(numVariables, denVariables);
        switchFractionsLocation(denVariables, numVariables);
        numVariables = VariableUtils.dissociate(numVariables);
        denVariables = VariableUtils.dissociate(denVariables);

        MergeManager manager = context.getMergeManager();
        MergeBehavior<Object> nullifying = manager.getByType(PairNullifying.class);
        manager.merge(numVariables, denVariables, nullifying, false, VariableComparator.COMPARATOR);
        manager.merge(aFloatList, bFloatList, nullifying);

        filterNull(numVariables, denVariables, aFloatList, bFloatList);

        MergeBehavior<Variable> joinBehavior = manager.getByType(VariableCombinationBehavior.class);
        numVariables = manager.merge(numVariables, joinBehavior, VariableComparator.COMPARATOR);
        denVariables = manager.merge(denVariables, joinBehavior, VariableComparator.COMPARATOR);

        aFloat = multJoin(aFloatList);
        bFloat = multJoin(bFloatList);
        Monomial finalNum = new Monomial(aFloat, numVariables);
        Monomial finalDen = new Monomial(bFloat, denVariables);
        List<ExpressionOverlay> finalOverlays = new ArrayList<>();
        if(!denVariables.isEmpty() || Math.abs(bFloat) != 1) {
            finalOverlays.add(FractionOverlay.fromExpression(Expression.of(finalDen)));
        }

        ComplexVariable finalComplex = new ComplexVariable(Collections.singletonList(finalNum), finalOverlays);
        return new Monomial(1, finalComplex);
    }

    private float multJoin(List<Float> list) {
        float f = 1;
        for(float current : list) f *= current;
        return f;
    }

    // Support if switched is a polynomial
    private void switchFractionsLocation(List<Variable> toTransfer, List<Variable> destination) {
        for(int i = 0; i < toTransfer.size(); i++) {
            Variable var = toTransfer.get(i);
            if(var instanceof  SingleCharVariable) continue;

            ComplexVariable complexVar = (ComplexVariable) var;
            if(complexVar.getOverlaysSize() == 0) continue;

            ExpressionOverlay last = complexVar.getOverlay(-1);
            if(last instanceof FractionOverlay) {
                List<Monomial> lastFracMonomials = ((FractionOverlay) last).getElements();
                if(lastFracMonomials.size() > 1) {LOGGER.warn("Impossible to revert fraction"); continue;}

                destination.addAll(lastFracMonomials.get(0).getVarPart().getVariables());
                List<ExpressionOverlay> currentVarOverlaysCopy = new ArrayList<>(complexVar.getOverlays());
                currentVarOverlaysCopy.remove(currentVarOverlaysCopy.size()-1);
                toTransfer.set(i, new ComplexVariable(complexVar.getElements(), currentVarOverlaysCopy));
            }
        }
    }

    private void filterNull(List... targets) {
        for(List list : targets) {
            list.removeAll(Collections.singleton(null));
        }
    }

    @Override
    public Expression inFormat(Expression origin) {
        return origin;
    }

    @Override
    public Expression outFormat(Expression origin) {
        return origin;
    }
}
