package net.akami.mask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionUtils.class);
    private static final StringBuilder BUILDER = new StringBuilder();
    private static final String DELETE_VARIABLES = "[a-zA-DF-Z]+";
    private static final String DELETE_NON_VARIABLES = "[\\d.+\\-/*()^]+";
    public static final String MATH_SIGNS = "+-*/^()";
    // 'E' deliberately missing
    public static final String VARIABLES = "abcdefghijklmnopqrstuvwxyzABCDFGHIJKLMNOPQRSTUVWXYZ";

    public static List<String> toMonomials(String self) {

        List<String> monomials = new ArrayList<>();
        int lastIndex = 0;
        for (int i = 0; i < self.length(); i++) {
            // We don't want i = 0 because if the first char is '-', it will add an empty string to the list
            if (self.charAt(i) == '+' || self.charAt(i) == '-' && i != 0) {
                if (ReducerFactory.isSurroundedByParentheses(i, self))
                    continue;
                String monomial = self.substring(lastIndex, i);
                monomials.add(monomial);
                LOGGER.debug("Added {} to monomials because sign found", monomial);
                lastIndex = i;
            }
            if (i == self.length() - 1) {
                String monomial = self.substring(lastIndex, i + 1);
                monomials.add(monomial);
                LOGGER.debug("Added {} to monomials because we reached the end of the String", monomial);
            }
        }
        return monomials;
    }

    public static String toVariables(String exp) {

        String[] unSortedVars = keepEachCharacterOnce(exp.replaceAll(DELETE_NON_VARIABLES, "")).split("");
        Arrays.sort(unSortedVars);
        String vars = String.join("", unSortedVars);
        if (vars.length() == 0)
            return "";

        LOGGER.debug("Looking for the variables of the sequence : {}", exp);
        List<String> finalVars = new ArrayList<>();
        StringBuilder builtExpression = new StringBuilder();
        builtExpression.append(exp);
        // TODO : use regex
        clearBuilder();
        for (int i = 0; i < vars.length(); i++) {
            int partsAmount = 0;
            char var = vars.charAt(i);

            LOGGER.debug("Treating {}", var);

            // can happen if 'x^y', the y has been transformed to '$', meaning it wasn't a variable but an exponent.
            if (!builtExpression.toString().contains(String.valueOf(var))) {
                LOGGER.debug("Variable {} not found. Skipping.", var);
                continue;
            }

            for (int j = 0; j < builtExpression.length(); j++) {

                char c1 = builtExpression.charAt(j);
                if (var == c1) {
                    partsAmount++;
                    LOGGER.debug("Identical variable found at index {} : {}", j, c1);
                    if (j + 2 > builtExpression.length() || builtExpression.charAt(j + 1) != '^') {
                        BUILDER.append("+1");
                        LOGGER.debug("No power found. Adds 1");
                    } else {
                        SequenceCalculationResult sequenceResult = sequenceAfterPow(j + 1, exp);
                        String exponent = sequenceResult.exponent;
                        String valuelessSequence = exponent.replaceAll(".", "\\$");
                        builtExpression.replace(sequenceResult.start, sequenceResult.end, valuelessSequence);
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
                LOGGER.debug("Several parts forming the power : {}. Needs extra sum", BUILDER.toString());
                String reducedExponent = MathUtils.sum(BUILDER.toString(), "");
                clearBuilder();
                BUILDER.append(reducedExponent);
            }
            String exponent = BUILDER.toString();
            if (!isReduced(exponent)) {
                exponent = "(" + exponent + ")";
            }
            LOGGER.debug("Final exponent calculation of {} : {}", var, exponent);
            if (exponent.equals("1")) {
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

    private static SequenceCalculationResult sequenceAfterPow(int powIndex, String fullExp) {

        SequenceCalculationResult result = new SequenceCalculationResult();

        boolean betweenBrackets = fullExp.charAt(powIndex + 1) == '(';

        for (int i = powIndex + 1; i < fullExp.length(); i++) {
            char c = fullExp.charAt(i);

            if (!betweenBrackets && "+-*/^".contains(String.valueOf(c))) {
                LOGGER.debug("Found an operator : {} at index {}. Stopped", c, i);
                result.exponent = fullExp.substring(powIndex + 1, i);
                result.start = powIndex + 1;
                result.end = i;
                break;
            }
            if (betweenBrackets && c == ')') {
                LOGGER.debug("Found the closing bracket");
                result.start = powIndex + 2;
                result.exponent = fullExp.substring(powIndex + 2, i);
                result.end = i;
                break;
            }
        }
        if (result.exponent == null) {
            result.exponent = fullExp.substring(powIndex + 1);
            result.start = powIndex;
            result.end = fullExp.length();
        }
        LOGGER.debug("Sequence {} after pow at index {} found : {}", fullExp, powIndex, result.exponent);
        return result;
    }

    // TODO : redo this ugly thing
    public static String toNumericValue(String self) {
        LOGGER.debug("Calculating the numeric value of {}", self);
        clearBuilder();
        self = self.replace("*", "");
        BUILDER.append(self);

        for (int i = 0; i < self.length(); i++) {
            char c = self.charAt(i);
            if (VARIABLES.contains(String.valueOf(c))) {
                LOGGER.debug("Before : {}", BUILDER);
                deleteExponentOf(i, self, BUILDER);
                LOGGER.debug("After : {}", BUILDER);
            }
        }
        String numericValue = BUILDER.toString().replace("$", "");
        String finalNumericValue = numericValue.isEmpty() ? "1" : numericValue;
        if (finalNumericValue.equals("-")) {
            finalNumericValue = "-1";
        } else if (finalNumericValue.equals("+")) {
            finalNumericValue = "1";
        }
        LOGGER.info("Numeric value of {} : {}", self, finalNumericValue);
        return finalNumericValue;
    }

    private static void deleteExponentOf(int i, String self, StringBuilder builder) {
        builder.setCharAt(i, '$');
        LOGGER.debug("Between 1 : {}", builder);

        if (!(i + 1 < self.length() && self.charAt(i + 1) == '^')) {
            return;
        }
        LOGGER.debug("FOUND A ^");
        // sets the '^' to '$'
        builder.setCharAt(i + 1, '$');
        LOGGER.debug("Between x : {}, after = {}", builder, self.charAt(i+2));
        if (self.charAt(i+2) == '(') {
            builder.setCharAt(i+2, '$');
            int openingBrackets = 0;
            for (int j = i + 3; j < self.length(); j++) {
                builder.setCharAt(j, '$');
                if (self.charAt(j) == ')' && openingBrackets <= 0) {
                    break;
                }
                if (self.charAt(j) == '(') {
                    LOGGER.debug("Opening bracket found at index {}", j);
                    openingBrackets++;
                }
            }
            LOGGER.debug("Between final : {}",builder);
        } else {
            for (int j = i + 2; j < self.length(); j++) {
                if (MATH_SIGNS.contains(String.valueOf(self.charAt(j)))) {
                    break;
                }
                builder.setCharAt(j, '$');
            }
        }

    }

    public static String keepEachCharacterOnce(String self) {
        List<String> chars = new ArrayList<>();
        for (char c : self.toCharArray()) {
            if (!chars.contains(String.valueOf(c)))
                chars.add(String.valueOf(c));
        }
        return String.join("", chars);
    }

    public static boolean isReduced(String self) {
        return !(self.contains("+") || self.contains("-") || self.contains("*") || self.contains("/")
                || self.contains("^"));
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    public static String cancelMultShortcut(String self) {
        clearBuilder();

        for(int i = 0; i < self.length(); i++) {
            String c = String.valueOf(self.charAt(i));
            if(ExpressionUtils.VARIABLES.contains(c) && i!= 0 &&
                    !ExpressionUtils.MATH_SIGNS.contains(String.valueOf(self.charAt(i-1)))) {
                BUILDER.append("*").append(c);
            } else {
                BUILDER.append(c);
            }
        }
        return BUILDER.toString();
    }

    public static String addMultShortcut(String self) {
        clearBuilder();
        BUILDER.append(self);
        for(int i = 1; i < self.length()-1; i++) {
            char c = self.charAt(i);

            if(c == '*' && VARIABLES.contains(String.valueOf(self.charAt(i+1)))) {
                BUILDER.setCharAt(i, '$');
            }

        }
        return BUILDER.toString().replace("$", "");
    }
    private static class SequenceCalculationResult {
        private String exponent;
        private int start;
        private int end;
    }
}
