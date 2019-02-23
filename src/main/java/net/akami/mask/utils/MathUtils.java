package net.akami.mask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

// TODO : add more temporary variables
public class MathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);
    private static final StringBuilder BUILDER = new StringBuilder();
    private static final String DELETE_VARIABLES = "[a-zA-DF-Z]+";
    private static final String DELETE_NON_VARIABLES = "[\\d.+\\-/*()^]+";
    private static final String NUMBERS = "0123456789";
    // 'E' deliberately missing
    private static final String VARIABLES = "abcdefghijklmnopqrstuvwxyzABCDFGHIJKLMNOPQRSTUVWXYZ";

    //TODO remove spaces
    public static String sum(String a, String b) {

        LOGGER.debug("Sum process : \n");
        List<String> monomials = toMonomials(a);
        monomials.addAll(toMonomials(b));

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

            String vars = toVariables(part);
            LOGGER.debug("Analyzing {}, found \"{}\" as variables", part, vars);
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
                if (toVariables(part2).equals(vars)) {
                    compatibleParts.put(new BigDecimal(toNumericValue(part2)), j);
                }
            }
            BigDecimal finalTotal = new BigDecimal(toNumericValue(part));

            for (BigDecimal value : compatibleParts.keySet()) {
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
        return result.startsWith("+") ? result.substring(1) : result;
    }

    public static String subtract(String a, String b) {
        LOGGER.debug("Subtraction process : \n");

        List<String> monomials = toMonomials(a);
        List<String> bMonomials = toMonomials(b);

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
        LOGGER.debug("Multiplication process : \n");

        List<String> aMonomials = toMonomials(a);
        List<String> bMonomials = toMonomials(b);

        // We can't use the constant BUILDER, because it is cleared repeatedly inside the loop
        StringBuilder builder = new StringBuilder();

        for (String part : aMonomials) {
            for (String part2 : bMonomials) {
                String result = simpleMult(part, part2);
                LOGGER.debug("Result of simple mult : {}", result);
                boolean first = part.equals(aMonomials.get(0)) && part2.equals(bMonomials.get(0));
                if (result.startsWith("+") || result.startsWith("-") || first) {
                    builder.append(result);
                } else {
                    builder.append("+" + result);
                }
            }
        }
        return builder.toString();
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

        String originalVars = toVariables(concatenated);

        BigDecimal aValue = new BigDecimal(toNumericValue(a));
        BigDecimal bValue = new BigDecimal(toNumericValue(b));
        String floatResult = cutSignificantZero(aValue.multiply(bValue).toString());

        return floatResult.equals("1") ? originalVars : floatResult + originalVars;
    }

    public static String divide(String a, String b) {
        LOGGER.debug("Operation process : \n");

        String vars = toVariables(a + b);

        if (vars.length() == 0) {
            BigDecimal bigA = new BigDecimal(a);
            BigDecimal bigB = new BigDecimal(b);
            String result = cutSignificantZero(bigA.divide(bigB).toString());
            return result;
        }
        return a + "/" + b;
    }

    public static String pow(String a, String b) {
        LOGGER.debug("Pow operation process : \n");

        String vars = toVariables(a + "^" + b);

        if (vars.length() == 0) {
            LOGGER.debug("No variable found, return a^b value");
            return String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
        }
        return a + "^" + (isReduced(b) ? b : "(" + b + ")");
    }

    public static List<String> toMonomials(String self) {

        List<String> monomials = new ArrayList<>();
        int lastIndex = 0;
        for (int i = 0; i < self.length(); i++) {
            // We don't want i = 0 because if the first char is '-', it will add an empty string to the list
            if (self.charAt(i) == '+' || self.charAt(i) == '-' && i != 0) {
                if(ReducerFactory.isSurroundedByParentheses(i, self))
                    continue;
                String monomial = self.substring(lastIndex, i);
                monomials.add(monomial);
                LOGGER.debug("Added {} because sign found", monomial);
                lastIndex = i;
            }
            if (i == self.length() - 1) {
                String monomial = self.substring(lastIndex, i + 1);
                monomials.add(monomial);
                LOGGER.debug("Added {} because we reached the end of the String", monomial);
            }
        }
        return monomials;
    }

    public static String toVariables(String exp) {

        String vars = keepEachCharacterOnce(exp.replaceAll(DELETE_NON_VARIABLES, ""));
        if (vars.length() == 0)
            return "";

        LOGGER.debug("Sequence : {}", exp);
        List<String> finalVars = new ArrayList<>();
        StringBuilder builtExpression = new StringBuilder();
        builtExpression.append(exp);
        // TODO : use regex
        clearBuilder();
        for (int i = 0; i < vars.length(); i++) {
            int partsAmount = 0;
            char var = vars.charAt(i);

            // can happen if 'x^y', the y has been transformed to '$', meaning it wasn't a variable but an exponent.
            if(!builtExpression.toString().contains(String.valueOf(var))) {
                LOGGER.debug("Variable {} not found. Skipping.", var);
                continue;
            }

            LOGGER.debug("Treating {}", var);
            for (int j = 0; j < builtExpression.length(); j++) {

                char c1 = builtExpression.charAt(j);
                if (var == c1) {
                    partsAmount++;
                    LOGGER.debug("Identical variable found at index {} : {}", j, c1);
                    if (j + 2 > builtExpression.length() || builtExpression.charAt(j + 1) != '^') {
                        BUILDER.append("+1");
                        LOGGER.debug("No power found. Adds 1");
                    } else {
                        String exponent = sequenceAfterPow(j+1, exp);
                        builtExpression.replace(j+2, j+2+exponent.length(), "$");
                        BUILDER.append("+").append(exponent);
                        LOGGER.debug("Power found. Adds {}", exponent);
                    }
                    builtExpression.setCharAt(j, '$');
                    LOGGER.debug("Sequence : {}", builtExpression.toString());
                    LOGGER.debug("Exponent : {}", BUILDER.toString());
                }
            }
            if (BUILDER.charAt(0) == '+')
                BUILDER.deleteCharAt(0);

            if (partsAmount > 1) {
                LOGGER.debug("Needs extra sum");
                String reducedExponent = sum(BUILDER.toString(), "");
                clearBuilder();
                BUILDER.append(reducedExponent);
            }
            String exponent = BUILDER.toString();
            if(!isReduced(exponent)) {
                exponent = "(" + exponent + ")";
            }
            LOGGER.debug("Final exponent calculation : {}", exponent);
            if(exponent.equals("1")) {
                finalVars.add(String.valueOf(var));
            } else {
                finalVars.add(var + "^" + exponent);
            }
            clearBuilder();
        }
        String result = String.join("", finalVars);

        LOGGER.debug("Result of toVariables(): {}", result);
        return result;
    }

    private static String sequenceAfterPow(int powIndex, String fullExp) {

        boolean betweenBrackets = fullExp.charAt(powIndex+1) == '(';

        for(int i = powIndex+1; i < fullExp.length(); i++) {
            char c = fullExp.charAt(i);

            if(!betweenBrackets && "+-*/^".contains(String.valueOf(c))) {
                LOGGER.debug("Found an operator. Stopped");
                return fullExp.substring(powIndex+1, i);
            }
            if(betweenBrackets && c == ')') {
                LOGGER.debug("Found the closing bracket");
                return fullExp.substring(powIndex + 2, i);
            }

        }
        return fullExp.substring(powIndex+1);
    }

    public static String keepEachCharacterOnce(String self) {
        List<String> chars = new ArrayList<>();
        for (char c : self.toCharArray()) {
            if (!chars.contains(String.valueOf(c)))
                chars.add(String.valueOf(c));
        }
        return String.join("", chars);
    }

    private static String cutSignificantZero(String self) {
        return self.endsWith(".0") ? self.substring(0, self.length() - 2) : self;
    }

    public static String toNumericValue(String self) {
        clearBuilder();
        BUILDER.append(self);

        for (int i = 0; i < self.length(); i++) {
            char c = self.charAt(i);
            if(VARIABLES.contains(String.valueOf(c))) {
                BUILDER.setCharAt(i, '$');

                if(i+1 < self.length() && self.charAt(i+1) == '^') {
                    BUILDER.setCharAt(i+1, '$');
                    if(self.charAt(i+2) == '(') {
                        for(int j = i+3; j < self.length(); j++) {
                            BUILDER.setCharAt(j, '$');
                            if(self.charAt(j) == ')') {
                                break;
                            }
                        }
                    }
                    BUILDER.setCharAt(i+2, '$');
                }
            }
        }
        String numericValue = BUILDER.toString().replace("$", "");
        String finalNumericValue = numericValue.isEmpty() ? "1" : numericValue;
        if (finalNumericValue.equals("-")) {
            finalNumericValue = "-1";
        } else if(finalNumericValue.equals("+")) {
            finalNumericValue = "1";
        }
        LOGGER.debug("Numeric value of {} : {}", self, finalNumericValue);
        return finalNumericValue;
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    private static boolean isReduced(String self) {
        return !(self.contains("+") || self.contains("-") || self.contains("*") || self.contains("/")
                || self.contains("^"));
    }
}
