package net.akami.mask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ExpressionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionUtils.class);
    private static final StringBuilder BUILDER = new StringBuilder();
    private static final String DELETE_VARIABLES = "[a-zA-DF-Z]+";
    private static final String DELETE_NON_VARIABLES = "[\\d.+\\-/*()^]+";
    // 'E' deliberately missing
    private static final String VARIABLES = "abcdefghijklmnopqrstuvwxyzABCDFGHIJKLMNOPQRSTUVWXYZ";

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

        String vars = keepEachCharacterOnce(exp.replaceAll(DELETE_NON_VARIABLES, ""));
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
                        SequenceCalculationResult sequenceResult = sequenceAfterPow(j+1, exp);
                        String exponent = sequenceResult.exponent;
                        String valuelessSequence = exponent.replaceAll(".", "\\$");
                        LOGGER.error(builtExpression.toString()+" / "+sequenceResult.start+" / "+sequenceResult.end);
                        builtExpression.replace(sequenceResult.start, sequenceResult.end, valuelessSequence);
                        LOGGER.error(builtExpression.toString());
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
                String reducedExponent = MathUtils.sum(BUILDER.toString(), "");
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

    private static SequenceCalculationResult sequenceAfterPow(int powIndex, String fullExp) {

        SequenceCalculationResult result = new SequenceCalculationResult();

        boolean betweenBrackets = fullExp.charAt(powIndex+1) == '(';

        for(int i = powIndex+1; i < fullExp.length(); i++) {
            char c = fullExp.charAt(i);

            if(!betweenBrackets && "+-*/^".contains(String.valueOf(c))) {
                LOGGER.debug("Found an operator : {} at index {}. Stopped", c, i);
                result.exponent = fullExp.substring(powIndex+1, i);
                result.start = powIndex+1;
                result.end = i;
                break;
            }
            if(betweenBrackets && c == ')') {
                LOGGER.debug("Found the closing bracket");
                result.start = powIndex+2;
                result.exponent = fullExp.substring(powIndex + 2, i);
                result.end = i;
                break;
            }
        }
        if(result.exponent == null) {
            result.exponent = fullExp.substring(powIndex + 1);
            result.start = powIndex;
            result.end = fullExp.length();
        }
        return result;
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

    private static class SequenceCalculationResult {
        private String exponent;
        private int start;
        private int end;
    }
}
