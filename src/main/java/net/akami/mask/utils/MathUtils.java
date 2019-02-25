package net.akami.mask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.*;

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
     *
     * @param monomials
     * @return the sum of all monomials given
     */
    public static String sum(List<String> monomials) {

        List<String> finalMonomials = new ArrayList<>();

        for (int i = 0; i < monomials.size(); i++) {
            String part = monomials.get(i);
            if (part == null)
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
                    if(compatibleParts.containsKey(toAdd)) {
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
            LOGGER.error(""+ExpressionUtils.toNumericValue(part));
            for (BigDecimal value : compatibleParts.keySet()) {
                LOGGER.debug("Value : "+value);
                finalTotal = finalTotal.add(value);
                // The compatible part is set to null in the list
                monomials.set(compatibleParts.get(value), null);
            }
            // The part itself is also set to null in the list
            monomials.set(i, null);

            String numericTotal = finalTotal.toString();
            if(numericTotal.equals("1") && !vars.isEmpty()) {
                finalMonomials.add(vars);
            } else if(numericTotal.equals("-1")){
                finalMonomials.add("-"+vars);
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
        return result.startsWith("+") ? result.substring(1) : result;
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
        return finalResult;
    }


    /**
     * Calculates a*b. both strings must not be polynomials. If you don't know whether a and b are monomials,
     * call {@link MathUtils#mult(String, String)} instead.
     * @param a the first value
     * @param b the second value
     * @return the result of the multiplication between a and b
     * @throws IllegalArgumentException if a and b are not monomials
     */
    public static String simpleMult(String a, String b) {

        String concatenated = a+"*"+b;
        /*String testThrow = concatenated.substring(1);
        if(testThrow.contains("+") || testThrow.contains("-"))
            throw new IllegalArgumentException("a and b must be monomials");*/

        String originalVars = ExpressionUtils.toVariables(concatenated);

        BigDecimal aValue = new BigDecimal(ExpressionUtils.toNumericValue(a));
        BigDecimal bValue = new BigDecimal(ExpressionUtils.toNumericValue(b));
        String floatResult = cutSignificantZero(aValue.multiply(bValue).toString());

        return floatResult.equals("1") && !originalVars.isEmpty() ? originalVars : floatResult + originalVars;
    }

    public static String divide(String a, String b) {
        LOGGER.info("Division process of {} |/| {}: \n", a, b);

        String vars = ExpressionUtils.toVariables(a + b);

        if (vars.length() == 0) {
            BigDecimal bigA = new BigDecimal(a);
            BigDecimal bigB = new BigDecimal(b);
            String result = cutSignificantZero(bigA.divide(bigB).toString());
            return result;
        }
        return a + "/" + b;
    }

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
        if(bVars.length() != 0 || (powValue = Float.parseFloat(b)) > 99) {
            LOGGER.info("Pow value contains variables or pow value is greater than 9. Returns a^b");
            return a + "^" + (ExpressionUtils.isReduced(b) ? b : "(" + b + ")");
        }

        clearBuilder();
        StringBuilder builder = new StringBuilder();
        builder.append(a);
        for(int i = 1; i < powValue; i++) {
            builder.replace(0, builder.length(), mult(builder.toString(), a));
            LOGGER.info("{} steps left. Currently : {}", powValue-i-1, builder.toString());
        }
        return builder.toString();
    }

    private static String cutSignificantZero(String self) {
        return self.endsWith(".0") ? self.substring(0, self.length() - 2) : self;
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
