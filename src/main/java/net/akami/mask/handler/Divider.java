package net.akami.mask.handler;

import net.akami.mask.expression.*;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.PairNullifying;
import net.akami.mask.core.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.VariableUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class Divider extends BinaryOperationHandler<Expression> {

    private static final MathContext CONTEXT = new MathContext(120);

    public Divider(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("Divider process of {} |/| {}: \n", a, b);

        if(b.length() == 1 && b.get(0) != null && b.get(0).getNumericValue() == 0)
            throw new IllegalArgumentException("Could not compute a division by zero");

        // Avoids division by zero error after simplifying all the elements.
        if(a.equals(b))
            return Expression.of(1);

        if (ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            // We are guaranteed that both expression have only one element, which is a monomial
            return Expression.of(numericalDivision(a.get(0), b.get(0)));
        }

        if(b.length() > 1) {
            LOGGER.error("Unable to calculate the division, the denominator being a polynomial. Returns a/b");
            return uncompletedDivision(a, b);
        }

        List<Monomial> finalElements = new ArrayList<>(a.length());

        for(Monomial numPart : a.getElements()) {
            finalElements.addAll(simpleDivision(numPart, b.get(0)));
        }

        return new Expression(finalElements);
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
        /*SimpleFraction[] fractions = new SimpleFraction[a.elementsLength()];
        int i = 0;

        for(Monomial element : a.getElements()) {
            fractions[i++] = new SimpleFraction(element, b);
        }

        return new Expression(fractions);*/return null;
    }

    public List<Monomial> simpleDivision(Monomial a, Monomial b) {
        if(ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            return Collections.singletonList(numericalDivision(a, b));
        }

        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        /*if(a instanceof SimpleFraction) {
            SimpleFraction aFrac = (SimpleFraction) a;
            Expression newDen = multiplier.operate(aFrac.getDenominator(), Expression.of(b));
            return Collections.singletonList(new SimpleFraction(aFrac.getNumerator(), newDen));
        }

        if(b instanceof SimpleFraction) {
            SimpleFraction bFrac = (SimpleFraction) b;
            Expression temporaryNum = multiplier.operate(Expression.of(a), bFrac.getDenominator());
            List<Monomial> finalElements = new ArrayList<>(temporaryNum.elementsLength());
            for(Monomial temporaryElement : temporaryNum.getElements()) {
                finalElements.addAll(simpleDivision(temporaryElement, ((SimpleFraction) b).getNumerator()));
            }
            return finalElements;
        }*/

        return Collections.singletonList(monomialDivision(a, b));
    }

    public Monomial monomialDivision(Monomial a, Monomial b) {

        if(a.equals(b)) return new NumberElement(1);

        float aNumeric = a.getNumericValue();
        float bNumeric = b.getNumericValue();

        List<Float> numValues = MathUtils.decomposeNumber(aNumeric, bNumeric);
        List<Variable> numVars = VariableUtils.dissociate(a.getVarPart());

        List<Float> denValues = MathUtils.decomposeNumber(bNumeric, aNumeric);
        List<Variable> denVars = VariableUtils.dissociate(b.getVarPart());

        MergeBehavior<Object> nullifying = MergeManager.getByType(PairNullifying.class);
        MergeManager.nonSortedMerge(numValues, denValues, nullifying, false);
        MergeManager.nonSortedMerge(numVars, denVars, nullifying, false);

        filter(numValues, numVars, denValues, denVars);

        float finalNumValue = 1;
        for(float f : numValues) finalNumValue *= f;

        float finalDenValue = 1;
        for(float f : denValues) finalDenValue *= f;

        if(finalDenValue == 1 && denVars.isEmpty())
            return new Monomial(finalNumValue, numVars);

        /*if(finalNumValue == 1 && numVars.isEmpty())
            return new SimpleFraction(new NumberElement(1), Expression.of(new Monomial(finalDenValue, denVars)));*/

        Monomial finalNumerator = new Monomial(finalNumValue, numVars);
        Expression finalDenominator = Expression.of(new Monomial(finalDenValue, denVars));
        return null;//new SimpleFraction(finalNumerator, finalDenominator);
    }

    private void filter(List... targets) {
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
