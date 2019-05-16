package net.akami.mask.handler;

import net.akami.mask.expression.*;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.PairNullifying;
import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.MathUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

public class Divider extends BinaryOperationHandler<Expression> {

    private static final MathContext CONTEXT = new MathContext(120);

    public Divider(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("Divider process of {} |/| {}: \n", a, b);

        if(b.length() == 1 && b.get(0) instanceof Monomial && ((Monomial) b.get(0)).getNumericValue() == 0)
            throw new IllegalArgumentException("Could not compute a division by zero");

        // Avoids division by zero error after simplifying all the elements.
        if(a.equals(b))
            return Expression.of(1);

        if (ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            // We are guaranteed that both expression have only one element, which is a monomial
            return Expression.of(numericalDivision((Monomial) a.get(0), (Monomial) b.get(0)));
        }

        if(b.length() > 1) {
            LOGGER.error("Unable to calculate the division, the denominator being a polynomial. Returns a/b");
            return uncompletedDivision(a, b);
        }

        List<ExpressionElement> finalElements = new ArrayList<>(a.length());

        for(ExpressionElement numPart : a.getElements()) {
            finalElements.addAll(simpleDivision(numPart, b.get(0)));
        }

        return new Expression(finalElements.toArray(new ExpressionElement[0]));
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
        SimpleFraction[] fractions = new SimpleFraction[a.length()];
        int i = 0;

        for(ExpressionElement element : a.getElements()) {
            fractions[i++] = new SimpleFraction(element, b);
        }

        return new Expression(fractions);
    }

    public List<ExpressionElement> simpleDivision(ExpressionElement a, ExpressionElement b) {
        if(ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            return Collections.singletonList(numericalDivision((Monomial) a, (Monomial) b));
        }

        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        if(a instanceof SimpleFraction) {
            SimpleFraction aFrac = (SimpleFraction) a;
            Expression newDen = multiplier.operate(aFrac.getDenominator(), Expression.of(b));
            return Collections.singletonList(new SimpleFraction(aFrac.getNumerator(), newDen));
        }

        if(b instanceof SimpleFraction) {
            SimpleFraction bFrac = (SimpleFraction) b;
            Expression temporaryNum = multiplier.operate(Expression.of(a), bFrac.getDenominator());
            List<ExpressionElement> finalElements = new ArrayList<>(temporaryNum.length());
            for(ExpressionElement temporaryElement : temporaryNum.getElements()) {
                finalElements.addAll(simpleDivision(temporaryElement, ((SimpleFraction) b).getNumerator()));
            }
            return finalElements;
        }

        Monomial monA = (Monomial) a;
        Monomial monB = (Monomial) b;
        return Collections.singletonList(monomialDivision(monA, monB));
    }

    // TODO : fill the method
    public ExpressionElement monomialDivision(Monomial a, Monomial b) {

        if(a.equals(b)) return new NumberElement(1);

        List<Float> numValues = MathUtils.decomposeNumber(a.getNumericValue());
        List<Variable> numVars = Variable.dissociate(a.getVariables());

        List<Float> denValues = MathUtils.decomposeNumber(b.getNumericValue());
        List<Variable> denVars = Variable.dissociate(b.getVariables());

        MergeBehavior<Object> nullifying = MergeManager.getByType(PairNullifying.class);
        MergeManager.merge(numValues, denValues, nullifying);
        MergeManager.merge(numVars, denVars, nullifying);

        filter(numValues, numVars, denValues, denVars);

        float finalNumValue = 1;
        for(float f : numValues) finalNumValue *= f;

        float finalDenValue = 1;
        for(float f : denValues) finalDenValue *= f;

        if(finalDenValue == 1 && denVars.isEmpty())
            return new Monomial(finalNumValue, numVars);

        if(finalNumValue == 1 && numVars.isEmpty())
            return new SimpleFraction(new NumberElement(1), Expression.of(new Monomial(finalDenValue, denVars)));

        Monomial finalNumerator = new Monomial(finalNumValue, numVars);
        Expression finalDenominator = Expression.of(new Monomial(finalDenValue, denVars));
        return new SimpleFraction(finalNumerator, finalDenominator);
    }

    private void filter(List... targets) {
        for(List list : targets) {
            list.removeAll(Collections.singleton(null));
        }
    }


    public String simpleDivision(String a, String b) {
        List<String> numFactors = ExpressionUtils.decompose(a);
        List<String> denFactors = ExpressionUtils.decompose(b);

        LOGGER.info("NumFactors : {}, DenFactors : {}", numFactors, denFactors);
        for (int i = 0; i < numFactors.size(); i++) {
            String numFactor = numFactors.get(i);
            if (numFactor == null) continue;

            for (int j = 0; j < denFactors.size(); j++) {
                String denFactor = denFactors.get(j);
                if (denFactor == null) continue;

                divideTwoFactors(numFactor, denFactor, i, j, numFactors, denFactors);
                numFactor = numFactors.get(i);
                if(numFactor == null)
                    break;
            }
        }
        LOGGER.info("Simple division proceeded. NumFactors : {}, DenFactors : {}", numFactors, denFactors);
        String finalNum = assembleFactors(numFactors);
        String finalDen = assembleFactors(denFactors);
        LOGGER.debug("Raw findResult : {} / {}", finalNum, finalDen);
        if(finalDen.isEmpty() || finalDen.equals("1") || finalDen.equals("1.0")) {
            return finalNum;
        }
        return finalNum + "/" + finalDen;
    }

    private void divideTwoFactors(String numFactor, String denFactor, int i, int j,
                                         List<String> numFactors, List<String> denFactors) {
        if (ExpressionUtils.isANumber(numFactor) && ExpressionUtils.isANumber(denFactor)) {
            LOGGER.info("{} and {} are numbers. Dividing them", numFactor, denFactor);
            proceedForNumericalDivision(numFactor, denFactor, i, j, numFactors, denFactors);
            LOGGER.info("Deleted at index {} and {} : NumFactors : {}, DenFactors : {}", i, j, numFactors, denFactors);

        } else {
            LOGGER.info("{} or {} isn't a number", numFactor, denFactor);
            proceedForVarDivision(numFactor, denFactor, i, j, numFactors, denFactors);
            LOGGER.debug("Deleted at index {} and {} : NumFactors : {}, DenFactors : {}", i, j, numFactors, denFactors);
        }
    }

    private void proceedForNumericalDivision(String num, String den, int i, int j, List<String> nums, List<String> dens) {
        LOGGER.debug("Numeric value found");
        if (MathUtils.cutSignificantZero(num).equals(MathUtils.cutSignificantZero(den))) {
            LOGGER.debug("Equal values at index {} and {} found. Deletes them", i, j);
            nums.set(i, null);
            dens.set(j, null);
        }
        LOGGER.info("Current state : Nums -> {}, Dens -> {}", nums, dens);
        LOGGER.debug("DenFactors : {}", dens);
        LOGGER.info("Both factors are numeric but not equal : {} and {}", num, den);
        float numValue = Float.parseFloat(num);
        float denValue = Float.parseFloat(den);
        float[] values = simplifyNumericalFraction(numValue, denValue);
        LOGGER.debug("Nums {}, Dens {}, i = {}, j = {}", nums, dens, i, j);
        nums.set(i, String.valueOf(values[0]));
        dens.set(j, String.valueOf(values[1]));
        LOGGER.info("Current state 2 : Nums -> {}, Dens -> {}", nums, dens);
    }

    private boolean proceedForVarDivision(String num, String den, int i, int j, List<String> nums, List<String> dens) {
        LOGGER.debug("Vars : {} and {}", num, den);
        String nVar = ExpressionUtils.toVariablesType(num);
        String dVar = ExpressionUtils.toVariablesType(den);

        if(!nVar.equals(dVar))
            return false;

        if(num.equals(den)) {
            LOGGER.debug("Both var values are equal. Deletes them");
            nums.set(i, null);
            dens.set(j, null);
            return true;
        }

        String nPow = num.replace(nVar+"^", "");
        String dPow = den.replace(dVar+"^", "");
        nPow = nPow.isEmpty() ? "1" : nPow;
        dPow = dPow.isEmpty() ? "1" : dPow;

        String subResult = null;//MathUtils.subtract(nPow, dPow, context);

        if(ExpressionUtils.isANumber(subResult)) {
            float subNumericResult = Float.parseFloat(subResult);
            if(subNumericResult < 0) {
                nums.set(i, null);
                dens.set(j, dVar + -subNumericResult);
            } else {
                dens.set(j, null);
                nums.set(i, nVar + subNumericResult);
            }
        } else {
            LOGGER.error("Unsupported operation for now");
        }
        return false;
    }

    private String assembleFactors(List<String> factors) {
        clearBuilder();
        BUILDER.append(1);
        for(String factor : factors) {
            if(factor != null && !factor.equals("1") && !factor.equals("1.0")) {
                Multiplier handler = context.getBinaryOperation(Multiplier.class);
                //TODO BUILDER.replace(0, BUILDER.length(), handler.simpleMult(BUILDER.toString(), factor));
            }
        }
        return BUILDER.toString();
    }

    private float[] simplifyNumericalFraction(float numerator, float denominator) {
        float[] values = new float[2];
        float numericResult;
        boolean simplified = false;
        if (Math.abs(numerator) > Math.abs(denominator)) {
            numericResult = floatDivision(numerator, denominator);
            if (numericResult % 1 == 0) {
                values[0] = numericResult;
                values[1] = 1;
                simplified = true;
            }
        } else {
            numericResult = floatDivision(denominator, numerator);
            if (numericResult % 1 == 0) {
                values[0] = 1;
                values[1] = numericResult;
                simplified = true;
            }
        }

        if (!simplified) {
            LOGGER.info("Couldn't simplify the numerical fraction {}/{}", numerator, denominator);
            values[0] = numerator;
            values[1] = denominator;
        }
        return values;
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
