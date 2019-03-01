package net.akami.mask.utils;

import net.akami.mask.structure.EquationCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.akami.mask.utils.ExpressionUtils.VARIABLES;
import static net.akami.mask.utils.ExpressionUtils.MATH_SIGNS;

import net.akami.mask.utils.ExpressionUtils.SequenceCalculationResult;

// TODO : add more temporary variables
public class MathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);
    private static final StringBuilder BUILDER = new StringBuilder();

    //TODO remove spaces
    public static String sum(String a, String b) {

        LOGGER.info("Sum process of {} |+| {}: \n", a, b);
        List<String> monomials = ExpressionUtils.toMonomials(a);
        monomials.addAll(ExpressionUtils.toMonomials(b));
        return sum(monomials);
    }

    /**
     * @param monomials
     * @return the sum of all monomials given
     */
    public static String sum(List<String> monomials) {

        List<String> finalMonomials = new ArrayList<>();

        for (int i = 0; i < monomials.size(); i++) {
            String part = monomials.get(i);
            if (part == null || part.isEmpty())
                continue;

            String vars = ExpressionUtils.toVariables(part);
            LOGGER.debug("Analyzing monomial {} : {}, found \"{}\" as variables", i, part, vars);
            // Adding all the "additionable" parts to the map, with their value and their index
            Map<BigDecimal, Integer> compatibleParts = new HashMap<>();

            for (int j = 0; j < monomials.size(); j++) {
                // We don't want to add the part itself
                if (i == j)
                    continue;

                String part2 = monomials.get(j);
                if (part2 == null)
                    continue;

                // If the unknown part is similar, we can add them
                if (ExpressionUtils.toVariables(part2).equals(vars)) {
                    BigDecimal toAdd = new BigDecimal(ExpressionUtils.toNumericValue(part2));
                    if (compatibleParts.containsKey(toAdd)) {
                        LOGGER.debug("Found copy in the map. Doubling the original.");
                        int index = compatibleParts.get(toAdd);
                        compatibleParts.remove(toAdd, index);
                        compatibleParts.put(toAdd.multiply(new BigDecimal("2")), index);
                    } else {
                        compatibleParts.put(toAdd, j);
                    }
                }
            }
            BigDecimal finalTotal = new BigDecimal(ExpressionUtils.toNumericValue(part));
            for (BigDecimal value : compatibleParts.keySet()) {
                LOGGER.debug("Value : " + value);
                finalTotal = finalTotal.add(value);
                // The compatible part is set to null in the list
                monomials.set(compatibleParts.get(value), null);
            }
            // The part itself is also set to null in the list
            monomials.set(i, null);

            String numericTotal = finalTotal.toString();
            if (numericTotal.equals("1") && !vars.isEmpty()) {
                finalMonomials.add(vars);
            } else if (numericTotal.equals("-1") && !vars.isEmpty()) {
                finalMonomials.add("-" + vars);

            } else {
                finalMonomials.add(cutSignificantZero(numericTotal + vars));
            }
        }

        finalMonomials.addAll(monomials);

        clearBuilder();
        for (String rest : finalMonomials) {
            if (rest == null)
                continue;

            if (rest.startsWith("+") || rest.startsWith("-")) {
                BUILDER.append(rest);
            } else {
                BUILDER.append("+" + rest);
            }
        }
        String result = BUILDER.toString();
        LOGGER.info("- Raw result of sum / subtraction : {}", result);
        return ExpressionUtils.cancelMultShortcut(result.startsWith("+") ? result.substring(1) : result);
    }

    public static String subtract(String a, String b) {
        LOGGER.info("Subtraction process of {} |-| {}: \n", a, b);

        List<String> monomials = ExpressionUtils.toMonomials(a);
        List<String> bMonomials = ExpressionUtils.toMonomials(b);

        // Changes the sign of the monomials that need to be subtracted
        for (int i = 0; i < bMonomials.size(); i++) {
            String m = bMonomials.get(i);

            if (m.startsWith("+")) {
                bMonomials.set(i, "-" + m.substring(1));
            } else if (m.startsWith("-")) {
                bMonomials.set(i, "+" + m.substring(1));
            } else {
                bMonomials.set(i, "-" + m);
            }
        }
        monomials.addAll(bMonomials);
        return sum(monomials);
    }

    public static String mult(String a, String b) {
        LOGGER.info("Multiplication process of {} |*| {}: \n", a, b);

        List<String> aMonomials = ExpressionUtils.toMonomials(a);
        List<String> bMonomials = ExpressionUtils.toMonomials(b);

        // We can't use the constant BUILDER, because it is cleared repeatedly inside the loop
        StringBuilder builder = new StringBuilder();

        for (String part : aMonomials) {
            for (String part2 : bMonomials) {
                LOGGER.debug("Treating simple mult : {} |*| {}", part, part2);
                String result = simpleMult(part, part2);
                LOGGER.info("Result of simple mult between {} and {} : {}", part, part2, result);
                boolean first = part.equals(aMonomials.get(0)) && part2.equals(bMonomials.get(0));
                if (result.startsWith("+") || result.startsWith("-") || first) {
                    builder.append(result);
                } else {
                    builder.append("+" + result);
                }
            }
        }
        String unReducedResult = builder.toString();
        String finalResult = sum(unReducedResult, "");
        LOGGER.info("- Result of mult {} |*| {} : {}", a, b, finalResult);
        return ExpressionUtils.cancelMultShortcut(finalResult);
    }


    /**
     * Calculates a*b. both strings must not be polynomials. If you don't know whether a and b are monomials,
     * call {@link MathUtils#mult(String, String)} instead.
     *
     * @param a the first value
     * @param b the second value
     * @return the result of the multiplication between a and b
     * @throws IllegalArgumentException if a and b are not monomials
     */
    public static String simpleMult(String a, String b) {

        String concatenated = a + "*" + b;
        String originalVars = ExpressionUtils.toVariables(concatenated);

        BigDecimal aValue = new BigDecimal(ExpressionUtils.toNumericValue(a));
        BigDecimal bValue = new BigDecimal(ExpressionUtils.toNumericValue(b));
        String floatResult = cutSignificantZero(aValue.multiply(bValue).toString());

        String finalResult = floatResult.equals("1") && !originalVars.isEmpty() ? originalVars : floatResult + originalVars;
        return ExpressionUtils.cancelMultShortcut(finalResult);
    }

    /*

        3x^2 / 6x -> 3 * x * x / 3 * 2 * x -> x/2
     */
    public static String divide(String a, String b) {
        LOGGER.info("Division process of {} |/| {}: \n", a, b);

        String concatenated = a + b;

        if (!VARIABLES.contains(concatenated) && !MATH_SIGNS.contains(concatenated)) {
            BigDecimal bigA = new BigDecimal(a);
            BigDecimal bigB = new BigDecimal(b);
            String result = cutSignificantZero(bigA.divide(bigB).toString());
            LOGGER.info("Result of {} / {} : {}", a, b, result);
            return result;
        }
        return a + "/" + b;
    }

    public static String simpleDivision(String a, String b) {
        List<String> numFactors = ExpressionUtils.decompose(a);
        List<String> denFactors = ExpressionUtils.decompose(b);

        for (int i = 0; i < numFactors.size(); i++) {
            String numFactor = numFactors.get(i);
            if (numFactor == null)
                continue;

            for (int j = 0; j < denFactors.size(); j++) {
                String denFactor = denFactors.get(j);
                if (denFactor == null)
                    continue;

                if (!VARIABLES.contains(numFactor) && !MATH_SIGNS.contains(numFactor)) {
                    LOGGER.debug("Numeric value found");
                    if (numFactor.equals(denFactor)) {
                        LOGGER.debug("Equal values at index {} and {} found. Deletes them", i, j);
                        numFactors.set(i, null);
                        denFactors.set(j, null);
                        break;
                    }
                    LOGGER.debug("DenFactors : {}", denFactors);
                    LOGGER.debug("Both factors are numeric but not equal : {} and {}", numFactor, denFactor);
                    float numValue = Float.parseFloat(numFactor);
                    float denValue = Float.parseFloat(denFactor);
                    float[] values = proceedSimplificationWithNumbers(numValue, denValue);
                    LOGGER.debug("Nums {}, Dens {}, i = {}, j = {}", numFactors, denFactors, i, j);
                    numFactors.set(i, String.valueOf(values[0]));
                    denFactors.set(j, String.valueOf(values[1]));
                    LOGGER.debug("DenFactors 2: {}", denFactors);
                }
            }
        }
        String finalNum = assembleFactors(numFactors);
        String finalDen = assembleFactors(denFactors);

        LOGGER.debug("Raw result : {} / {}", finalNum, finalDen);
        if(finalDen.isEmpty() || finalDen.equals("1") || finalDen.equals("1.0")) {
            return finalNum;
        }
        return finalNum + "/" + finalDen;
    }

    private static String assembleFactors(List<String> factors) {
        clearBuilder();
        BUILDER.append(1);
        for(String factor : factors) {
            if(factor != null && !factor.equals("1") && !factor.equals("1.0")) {
                BUILDER.replace(0, BUILDER.length(), MathUtils.simpleMult(BUILDER.toString(), factor));
            }
        }
        return BUILDER.toString();
    }

    private static float[] proceedSimplificationWithNumbers(float numerator, float denominator) {
        float[] values = new float[2];
        float numericResult;
        boolean simplified = false;
        if (numerator > denominator) {
            numericResult = Float.parseFloat(divide("" + numerator, "" + denominator));
            if (numericResult % 1 == 0) {
                values[0] = numericResult;
                values[1] = 1;
                simplified = true;
            }
        } else {
            numericResult = Float.parseFloat(divide("" + denominator, "" + numerator));
            if (numericResult % 1 == 0) {
                values[0] = 1;
                values[1] = numericResult;
                simplified = true;
            }
        }

        if (!simplified) {
            values[0] = numerator;
            values[1] = denominator;
        }
        return values;
    }

    // TODO : optimize : use other method to chain multiplications
    public static String pow(String a, String b) {
        LOGGER.debug("Pow operation process between {} and {} : \n", a, b);

        String aVars = ExpressionUtils.toVariables(a);
        String bVars = ExpressionUtils.toVariables(b);

        LOGGER.debug("aVars : {}, bVars : {}", aVars, bVars);
        if (aVars.length() == 0 && bVars.length() == 0) {
            String result = String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
            LOGGER.info("No variable found, return a^b value : {}", result);
            return result;
        }
        float powValue;
        // If pow value is too high, there is no point in developing the entire expression
        if (bVars.length() != 0 || (powValue = Float.parseFloat(b)) > 199) {
            LOGGER.info("Pow value contains variables or pow value is greater than 9. Returns a^b");
            return a + "^" + (ExpressionUtils.isReduced(b) ? b : "(" + b + ")");
        }

        clearBuilder();
        StringBuilder builder = new StringBuilder();
        builder.append(a);
        for (int i = 1; i < powValue; i++) {
            builder.replace(0, builder.length(), mult(builder.toString(), a));
            LOGGER.info("{} steps left. Currently : {}", powValue - i - 1, builder.toString());
        }
        return builder.toString();
    }

    /**
     * Method currently in development. Do not use
     *
     * @param a
     * @param b
     * @return
     */
    public static String highPow(String a, String b) {
        String aVars = ExpressionUtils.toVariables(a);
        String bVars = ExpressionUtils.toVariables(b);

        LOGGER.debug("aVars : {}, bVars : {}", aVars, bVars);
        if (aVars.length() == 0 && bVars.length() == 0) {
            String result = String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
            LOGGER.info("No variable found, return a^b value : {}", result);
            return result;
        }
        float powValue;

        if (bVars.length() != 0 || (powValue = Float.parseFloat(b)) > 199) {
            LOGGER.info("Pow value contains variables or pow value is greater than 9. Returns a^b");
            return a + "^" + (ExpressionUtils.isReduced(b) ? b : "(" + b + ")");
        }

        if (!a.startsWith("(") && !a.endsWith(")")) {
            a = "(" + a + ")";
        }
        StringBuilder finalResult = new StringBuilder();
        Map<String, String> calculatedValues = new HashMap<>();
        for (int i = 0; i < powValue; i++) {
            finalResult.append(a);
        }
        LOGGER.error(finalResult.toString());
        // Amount of required reductions = powValue - 1
        int start = -1;
        for (int i = 1; i < powValue; i++) {

            if (start + 1 >= finalResult.length()) {
                start = -1;
            }
            SequenceCalculationResult result1 = ExpressionUtils.groupAfter(start, finalResult.toString());
            int veryStart = start;
            start = result1.getEnd();

            LOGGER.error("Result1 : {} from {}, i = {}", result1.getSequence(), finalResult, veryStart);
            if (start + 1 >= finalResult.length()) {
                start = -1;
                finalResult.append(result1.getSequence());
                continue;
            }
            SequenceCalculationResult result2 = ExpressionUtils.groupAfter(start, finalResult.toString());
            LOGGER.error("Result2 : {} from {}, i = {}", result2.getSequence(), finalResult, start);
            start = result2.getEnd();

            String s1 = result1.getSequence();
            String s2 = result2.getSequence();
            String concatenated = s1 + ";" + s2;
            LOGGER.error("Treating {} times {}", s1, s2);
            if (calculatedValues.keySet().contains(concatenated)) {
                String foundResult = calculatedValues.get(concatenated);
                LOGGER.error("Found similar calculation. Added {}", foundResult);
                finalResult.replace(veryStart + 1, start + 1, "(" + foundResult + ")");
            } else {
                String calculatedResult = mult(s1, s2);
                LOGGER.error(finalResult.toString());
                LOGGER.error("No similar calculation found. Added {} instead of {} to {} in {}",
                        calculatedResult, veryStart + 1, start + 1, finalResult);
                finalResult.replace(veryStart + 1, start + 1, "(" + calculatedResult + ")");
                calculatedValues.put(s1 + ";" + s2, calculatedResult);
                LOGGER.error("Builder is now {}", finalResult);
                start = veryStart + calculatedResult.length() + 2;
                LOGGER.error("Next calculation will start from {}", start);
            }
        }
        return finalResult.toString();
    }

    private static String cutSignificantZero(String self) {
        return self.endsWith(".0") ? self.substring(0, self.length() - 2) : self;
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
