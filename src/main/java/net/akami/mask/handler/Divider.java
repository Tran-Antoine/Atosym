package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.merge.*;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.overlay.property.CommonDenominatorAdditionProperty;
import net.akami.mask.overlay.property.DivisionOfFractionsProperty;
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
        addDefaultProperties();
    }

    private void addDefaultProperties() {
        propertyManager.addProperty(new DivisionOfFractionsProperty(context));
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("Divider process of {} |/| {}: \n", a, b);

        if(b.length() == 1 && b.get(0) != null && b.get(0).getNumericValue() == 0)
            throw new IllegalArgumentException("Cannot compute a division by zero");

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
            finalElements.addAll(monomialDivision(numPart, b.get(0)));
        }

        return chainFinalElements(finalElements);
    }

    private Expression chainFinalElements(List<Monomial> finalElements) {
        CommonDenominatorAdditionProperty property = new CommonDenominatorAdditionProperty(context);
        MergeBehavior<Monomial> behavior = new SimpleDenominatorAdditionMerge(context, property);
        MergeManager mergeManager = context.getMergeManager();

        List<Monomial> newFinalElements = mergeManager.secureMerge(finalElements, behavior);
        return new Expression(newFinalElements);
    }

    public NumberElement numericalDivision(Monomial a, Monomial b) {
        float result = floatDivision(a.getNumericValue(), b.getNumericValue());
        LOGGER.info("Numeric division. Result of {} / {} : {}", a, b, result);
        return new NumberElement(result);
    }

    public float floatDivision(float a, float b) {
        BigDecimal bigA = new BigDecimal(a, context.getMathContext());
        BigDecimal bigB = new BigDecimal(b, context.getMathContext());
        return bigA.divide(bigB, context.getMathContext()).floatValue();
    }

    private Expression uncompletedDivision(Expression a, Expression b) {
        FractionOverlay overlay = FractionOverlay.fromExpression(b);
        ComplexVariable result = new ComplexVariable(a.getElements(), overlay);
        return Expression.of(new Monomial(1, result));
    }

    public List<Monomial> monomialDivision(Monomial a, Monomial b) {
        if(a.isSimple() && b.isSimple()) {
            return Collections.singletonList(simpleMonomialDivision(a, b));
        }
        return complexDivision(a, b);
    }

    private Monomial simpleMonomialDivision(Monomial a, Monomial b) {
        return simpleDivision(a.getNumericValue(), b.getNumericValue(), a.getVarPart().getVariables(), b.getVarPart().getVariables());
    }

    private List<Monomial> complexDivision(Monomial a, Monomial b) {
        OverlayDivisionMerge merge = new OverlayDivisionMerge(a, b, context, propertyManager.getProperties());
        return merge.merge();
    }

    public Monomial simpleDivision(float a, float b, List<Variable> decomposedA, List<Variable> decomposedB) {
        List<Float> numeratorFloats = MathUtils.decomposeNumber(a, b);
        List<Float> denominatorFloats = MathUtils.decomposeNumber(b, a);
        List<Variable> numeratorVars = new ArrayList<>(decomposedA);
        List<Variable> denominatorVars = new ArrayList<>(decomposedB);

        MergeManager mergeManager = context.getMergeManager();
        MergeBehavior<Object> nullify = mergeManager.getByType(PairNullifying.class);

        mergeManager.merge(numeratorFloats, denominatorFloats, nullify);
        mergeManager.merge(numeratorVars, denominatorVars, nullify,false, VariableComparator.COMPARATOR);

        filterNull(numeratorFloats, denominatorFloats, numeratorVars, denominatorVars);
        numeratorVars = VariableUtils.combine(numeratorVars, numeratorVars, context,true);
        denominatorVars = VariableUtils.combine(denominatorVars, denominatorVars, context, true);

        float finalNumFloat = multJoin(numeratorFloats);
        Monomial monomialNum = new Monomial(finalNumFloat, numeratorVars);
        if(denominatorFloats.isEmpty() & denominatorVars.isEmpty()) {
            return monomialNum;
        }

        float finalDenFloat = multJoin(denominatorFloats);

        FractionOverlay overlay = FractionOverlay.fromExpression(Expression.of(new Monomial(finalDenFloat, denominatorVars)));
        Monomial insights = new Monomial(finalNumFloat, monomialNum.getVarPart());
        return new Monomial(1, new ComplexVariable(insights, overlay));
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

}
