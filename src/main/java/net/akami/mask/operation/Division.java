package net.akami.mask.operation;

import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.MathUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class Division extends BinaryOperationHandler {

    private static final Division INSTANCE = new Division();

    @Override
    protected String operate(String a, String b) {
        // Avoids division by zero error.
        if(a.equals(b))
            return "1";

        LOGGER.info("Division process of {} |/| {}: \n", a, b);

        if (ExpressionUtils.isANumber(a) && ExpressionUtils.isANumber(b)) {
            BigDecimal bigA = new BigDecimal(a);
            BigDecimal bigB = new BigDecimal(b);
            String result = MathUtils.cutSignificantZero(bigA.divide(bigB, MathContext.DECIMAL128).toString());
            LOGGER.info("Numeric division. Result of {} / {} : {}", a, b, result);
            return result;
        }

        List<String> numMonomials = ExpressionUtils.toMonomials(a);
        int index = 0;
        for(String numMonomial : numMonomials) {
            String divisionResult = simpleDivision(numMonomial, b);
            LOGGER.info("Result of simple division between {} and {} : {}", numMonomial, b, divisionResult);
            if(divisionResult.startsWith("+") || divisionResult.startsWith("-") || index == 0) {
                numMonomials.set(index++, divisionResult);
            } else {
                numMonomials.set(index++, "+" + divisionResult);
            }
        }
        String divisionResult = String.join("", numMonomials);
        LOGGER.info("++++ Result of division between {} and {} : {}", a, b, divisionResult);
        return divisionResult;
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
            }
        }
        LOGGER.info("Simple division proceeded. NumFactors : {}, DenFactors : {}", numFactors, denFactors);
        String finalNum = assembleFactors(numFactors);
        String finalDen = assembleFactors(denFactors);
        LOGGER.debug("Raw result : {} / {}", finalNum, finalDen);
        if(finalDen.isEmpty() || finalDen.equals("1") || finalDen.equals("1.0")) {
            return finalNum;
        }
        return finalNum + "/" + finalDen;
    }

    private void divideTwoFactors(String numFactor, String denFactor, int i, int j,
                                         List<String> numFactors, List<String> denFactors) {
        String[] result = new String[2];
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

        String subResult = Subtraction.getInstance().rawOperate(nPow, dPow);

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
                BUILDER.replace(0, BUILDER.length(), Multiplication.getInstance().simpleMult(BUILDER.toString(), factor));
            }
        }
        return BUILDER.toString();
    }

    private float[] simplifyNumericalFraction(float numerator, float denominator) {
        float[] values = new float[2];
        float numericResult;
        boolean simplified = false;
        if (Math.abs(numerator) > Math.abs(denominator)) {
            numericResult = Float.parseFloat(rawOperate("" + numerator, "" + denominator));
            if (numericResult % 1 == 0) {
                values[0] = numericResult;
                values[1] = 1;
                simplified = true;
            }
        } else {
            numericResult = Float.parseFloat(rawOperate("" + denominator, "" + numerator));
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
    public String inFormat(String origin) {
        return origin;
    }

    @Override
    public String outFormat(String origin) {
        return ExpressionUtils.addMultShortcut(origin);
    }

    public static Division getInstance() {
        return INSTANCE;
    }
}
