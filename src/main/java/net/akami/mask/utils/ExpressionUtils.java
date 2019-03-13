package net.akami.mask.utils;

import net.akami.mask.structure.EquationSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExpressionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionUtils.class);
    private static final StringBuilder BUILDER = new StringBuilder();

    public static final String MATCH_POW_STRUCT = "(\\^([0-9.]+|\\(.*?\\)|(1|)[a-zA-DF-Z])|)";
    public static final String KEEP_VARIABLES_ONLY = "("+ "[a-zA-DF-Z]"+MATCH_POW_STRUCT+ "|" + "\\(\\(.*?\\)[@#§]\\)"+MATCH_POW_STRUCT + ")+";
    public static final String MATH_SIGNS = "+-*/^()";
    public static final String NUMBERS = "0123456789";
    // 'E' deliberately missing, because it corresponds to "*10^x"
    public static final String VARIABLES = "abcdefghijklmnopqrstuvwxyzABCDFGHIJKLMNOPQRSTUVWXYZ";
    public static final String TRIGONOMETRY_SHORTCUTS = "@#§";

    public static List<String> toMonomials(String self) {

        while (ExpressionUtils.areEdgesBracketsConnected(self)) {
            self = self.substring(1, self.length() - 1);
        }

        List<String> monomials = new ArrayList<>();
        int lastIndex = 0;
        for (int i = 0; i < self.length(); i++) {
            // We don't want i = 0 because if the first char is '-', it will add an empty string to the list
            if (self.charAt(i) == '+' || self.charAt(i) == '-' && i != 0) {
                if (isSurroundedByParentheses(i, self)) {
                    continue;
                }
                String monomial = self.substring(lastIndex, i);
                if (toNumericValue(monomial).equals("0")) continue;

                monomials.add(monomial);
                LOGGER.debug("Added {} to monomials because sign found", monomial);
                lastIndex = i;
            }
            if (i == self.length() - 1) {
                String monomial = self.substring(lastIndex, i + 1);
                if (toNumericValue(monomial).equals("0")) continue;

                monomials.add(monomial);
                LOGGER.debug("Added {} to monomials because we reached the end of the String", monomial);
            }
        }
        return monomials;
    }

    public static String toVariables(String exp) {

        if (!exp.matches("(.+|)[a-zA-DF-Z](.+|)"))
            return "";

        String[] vars = clearNonVariables(exp);

        Map<String, String> reducedVars = new HashMap<>();

        for (String var : vars) {
            String[] varExp = var.split("\\^", 2);
            if (reducedVars.containsKey(varExp[0])) {
                reducedVars.put(varExp[0], MathUtils.sum(reducedVars.get(varExp[0]), varExp[1]));
            } else {
                reducedVars.put(varExp[0], varExp[1]);
            }
        }

        clearBuilder();

        for (String result : reducedVars.keySet()) {
            String exponent = reducedVars.get(result);
            BUILDER.append(result);
            if (!exponent.equals("1")) {
                BUILDER.append('^');
                if (isReduced(exponent) || ExpressionUtils.areEdgesBracketsConnected(exponent)) {
                    BUILDER.append(exponent);
                } else {
                    BUILDER.append('(').append(exponent).append(')');
                }
            }
        }
        return BUILDER.toString();
    }

    public static boolean isExpressionTrigonometric(String exp) {
        return exp.contains("@") || exp.contains("#") || exp.contains("§");
    }

    public static boolean isTrigoShortcut(String exp) {
        return exp.equals("@") || exp.equals("#") || exp.equals("§");
    }

    /**
     * Removes the unnecessary chars to exp, then returns an array of all the variables found.
     * Example :
     * <p>
     * 5x^2x(y)3((x)@) -> [x^2, x, (y), ((x)@)]. If you want your variables to be simplified (x^2, x would become x^3),
     * use {@link ExpressionUtils#toVariables(String)} instead.
     *
     * @param exp the expression itself
     * @return an array containing the non-reduced vars.
     */
    public static String[] clearNonVariables(String exp) {
        clearBuilder();
        // Deletes all the useless characters

        Pattern pattern = Pattern.compile(KEEP_VARIABLES_ONLY);
        Matcher matcher = pattern.matcher(exp);
        while (matcher.find()) {
            String found = matcher.group();
            if (found.matches("(.+|)[a-zA-DF-Z](.+|)"))
                BUILDER.append(matcher.group());
        }
        // Replaces x^2x((x)#) by x^2 * x * ((x)#)
        String result = BUILDER.toString();
        clearBuilder();
        result = cancelMultShortcut(result);

        String[] varsResult = result.split("\\*");
        // Replaces x by x^1
        for (int i = 0; i < varsResult.length; i++) {
            String actual = varsResult[i];
            if (!actual.isEmpty() && !actual.contains("^"))
                varsResult[i] = actual + "^" + 1;
        }

        return varsResult;
    }

    /**
     * groupAfter(1, "3^(4+x)") will return 3 and 6 as start and end
     *
     * @param index
     * @param exp
     * @return
     */
    public static SequenceCalculationResult groupAfter(int index, String exp) {
        if (index < exp.length() - 1 && exp.charAt(index + 1) == '(') {
            return bracketSequenceAfter(index, exp);
        }
        LOGGER.debug("No bracket sequence after char at index {}", index);
        SequenceCalculationResult result = new SequenceCalculationResult();
        for (int i = index + 1; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if ("+-*/^".contains(String.valueOf(c))) {
                LOGGER.debug("Found an operator : {} at index {}. Stopped", c, i);
                result.sequence = exp.substring(index + 1, i);
                result.start = index + 1;
                result.end = i;
                break;
            }

            if (i == exp.length() - 1) {
                LOGGER.debug("Reached the end of the string.");
                result.sequence = exp.substring(index + 1);
                result.start = index + 1;
                result.end = exp.length();
            }
        }
        //removeBrackets(result, exp, index, true);
        LOGGER.debug("Result of group at index {} for {} : {}", index, exp, result.sequence);
        return result;
    }

    public static SequenceCalculationResult bracketSequenceAfter(int index, String exp) {

        SequenceCalculationResult result = new SequenceCalculationResult();

        int openingBracketsCount = 0;
        for (int i = index + 1; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (c == ')') {
                if (openingBracketsCount <= 0) {
                    LOGGER.debug("Found the closing bracket");
                    result.start = index + 2;
                    result.sequence = exp.substring(index + 2, i);
                    result.end = i;
                    break;
                } else {
                    openingBracketsCount--;
                }
            }
            if (c == '(' && i != index + 1) {
                openingBracketsCount++;
            }
        }

        //removeBrackets(result, exp, index, true);
        LOGGER.debug("Sequence {} after pow at index {} found : {}", exp, index, result.sequence);
        return result;
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
        self = self.replaceAll("\\s", "");
        clearBuilder();

        for (int i = 0; i < self.length(); i++) {
            String c = String.valueOf(self.charAt(i));
            boolean isVar = ExpressionUtils.VARIABLES.contains(c);

            if (isVar && i != 0 && !ExpressionUtils.MATH_SIGNS.contains(String.valueOf(self.charAt(i - 1)))) {
                BUILDER.append("*").append(c);
            } else if (i != 0 && c.equals("(") &&
                    (self.charAt(i - 1) == ')' || !MATH_SIGNS.contains(String.valueOf(self.charAt(i - 1))))) {
                BUILDER.append("*").append(c);
            } else if (i != 0 && self.charAt(i - 1) == ')' && !MATH_SIGNS.contains(c) && !TRIGONOMETRY_SHORTCUTS.contains(c)) {
                BUILDER.append("*").append(c);
            } else {
                BUILDER.append(c);
            }
        }
        LOGGER.error("Converted : " + self + " to " + BUILDER.toString());
        return BUILDER.toString();
    }

    public static String addMultShortcut(String self) {
        clearBuilder();
        BUILDER.append(self);
        for (int i = 1; i < self.length() - 1; i++) {
            char c = self.charAt(i);

            if (c == '*' && VARIABLES.contains(String.valueOf(self.charAt(i + 1)))) {
                BUILDER.setCharAt(i, '$');
            }

        }
        return BUILDER.toString().replace("$", "");
    }

    public static String toVariablesType(List<EquationSolver.BiMask> biMasks) {
        clearBuilder();
        for (EquationSolver.BiMask biMask : biMasks) {
            String line = biMask.getLeft().getExpression() + biMask.getRight().getExpression();
            BUILDER.append(line);
        }
        return toVariablesType(BUILDER.toString());
    }

    public static String toVariablesType(String self) {
        String letters = self.replaceAll("[\\d.+\\-*/^()@#§]+", "");
        Set<String> chars = new HashSet<>();
        for (char c : letters.toCharArray()) {
            chars.add(String.valueOf(c));
        }
        return String.join("", chars);
    }

    /**
     * @param exp an already reduced expression
     * @return
     */
    public static int getMaximalNumericPower(String exp) {
        List<String> monomials = toMonomials(exp);
        int highest = 0;

        for (String monomial : monomials) {
            if (!monomial.contains("^")) {
                highest = highest < 1 ? 1 : highest;
                continue;
            }
            String pow = groupAfter(monomial.indexOf('^'), monomial).sequence;
            if (pow.matches("[\\d+-]")) {
                int value = Integer.parseInt(pow.replaceAll("\\s", ""));
                if (value > highest) {
                    highest = value;
                }
            }
        }
        return highest;
    }
    // TODO : support (x+1) denominator for instance

    /**
     * @param exp an already reduced expression
     * @return
     */
    public static List<String> decompose(String exp) {
        LOGGER.info("Now decomposing expression {}", exp);
        List<String> elements = new ArrayList<>();
        elements.addAll(Arrays.asList(cancelMultShortcut(exp).split("\\*")));
        List<String> decomposedElements = new ArrayList<>();
        // We can't use a classic for each loop since the size of the list will be modified
        for (int i = 0; i < elements.size(); i++) {
            String element = elements.get(i);
            if (isANumber(element)) {
                List<String> decomposedLocal = decomposeNumber(Float.parseFloat(element));
                LOGGER.info("Decomposed {}, result : {}", element, decomposedLocal);
                elements.set(i, null);
                decomposedElements.addAll(decomposedLocal);
            } else {
                LOGGER.debug("{} is not a number", element);
                if (element.length() > 2 && element.charAt(1) == '^') {
                    decomposePoweredVariable(element, i, elements, decomposedElements);
                }
            }
        }
        elements.addAll(decomposedElements);
        elements = elements.stream().filter(x -> x != null && !x.equals("1.0")).collect(Collectors.toList());
        LOGGER.debug("Elements of {} : {}", exp, elements);
        return elements;
    }

    private static void decomposePoweredVariable(String origin, int i, List<String> in, List<String> out) {
        String exponent = origin.substring(2);
        while (ExpressionUtils.areEdgesBracketsConnected(exponent))
            exponent = exponent.substring(1, exponent.length() - 1);
        if (ExpressionUtils.isANumber(exponent)) {
            float exponentValue = Float.parseFloat(exponent);
            if (exponentValue % 1 != 0)
                return;

            in.set(i, null);
            for (int j = 0; j < exponentValue; j++) {
                out.add(String.valueOf(origin.charAt(0)));
            }
        }
    }

    public static List<String> decomposeNumber(float self) {
        LOGGER.info("Now decomposing float {}", self);

        List<String> results = new ArrayList<>();
        if (self % 1 != 0) {
            LOGGER.info("Non-integer given, returns it");
            results.add(String.valueOf(self));
            return results;
        }
        List<Integer> dividers = new ArrayList<>();
        if (self < 0) {
            results.add("-1");
            self *= -1;
        }
        // Builds the dividers array
        for (int i = 2; i <= self; i++) {
            boolean unique = true;
            for (int j = 2; j < i; j++) {
                if (i % j == 0 && (j != i && i != 2)) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                dividers.add(i);
                continue;
            }
        }
        int index = 0;
        while (index < dividers.size()) {
            int divider = dividers.get(index);
            if (self % divider == 0) {
                LOGGER.error("{} is a divider of {}", divider, self);
                results.add(String.valueOf(divider));
                self /= divider;
            } else {
                LOGGER.error("{} is not a divider of {}", divider, self);
                index++;
            }
        }
        LOGGER.error("Result : {}", results);
        return results;
    }

    public static boolean isANumber(String exp) {
        if (exp.length() == 0)
            return false;
        return exp.substring(1).matches("[\\d.]+") || NUMBERS.contains(exp);
    }

    public static boolean isSigned(String exp) {
        return exp.charAt(0) == '+' || exp.charAt(0) == '-';
    }

    public static boolean areEdgesBracketsConnected(String exp) {
        if (exp.isEmpty() || exp.charAt(0) != '(') {
            return false;
        }

        if(exp.length() > 1 && ExpressionUtils.TRIGONOMETRY_SHORTCUTS.contains(String.valueOf(exp.charAt(exp.length()-2)))) {
            return false;
        }

        int left = 0;
        for (int i = 1; i < exp.length() - 1; i++) {
            if (exp.charAt(i) == ')') {
                left--;
            } else if (exp.charAt(i) == '(') {
                left++;
            }
            if (left < 0) {
                break;
            }
        }
        if (left >= 0) {
            exp = exp.substring(1, exp.length() - 1);
            LOGGER.debug("Connected brackets found at position 0 and last, new expression : {}", exp);
            return true;
        }
        return false;
    }

    public static boolean isSurroundedByParentheses(int index, String exp) {

        // In case the exp is 5*-3 or 5/-3
        if (index > 0 && (exp.charAt(index - 1) == '/' || exp.charAt(index - 1) == '*')) {
            LOGGER.info("Character right after * or /. Is surrounded = true");
            return true;
        }

        int leftParenthesis = 0;

        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) == '(') {
                leftParenthesis++;
            }

            if (exp.charAt(i) == ')') {
                leftParenthesis--;
            }
            if (leftParenthesis > 0 && i == index) {
                LOGGER.debug("- Indeed surrounded");
                return true;
            }
        }
        LOGGER.debug("- Not surrounded");
        return false;
    }

    public static class SequenceCalculationResult {
        private String sequence;
        private int start;
        private int end;

        public String getSequence() {
            return sequence;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }

    public static String toNumericValue(String exp) {
        if (exp.isEmpty())
            return "0";

        exp = ExpressionUtils.addMultShortcut(exp)
                // deletes trigonometry stuff
                .replaceAll("\\(\\(.+\\)([@#§])\\)"+"(\\^([0-9.]+|((1|)[a-zA-DF-Z]))|)", "")
                // deletes variables + their pow value
                .replaceAll("[a-zA-DF-Z]\\^(([a-zA-DF-Z0-9^]+)|\\((.+\\)))", "")
                .replaceAll("(\\*[a-zA-DF-Z])|[a-zA-DF-Z]", "")
                .replaceAll("\\s", "");

        if (exp.equals("-") || exp.equals("+")) {
            exp = exp + "1";
        }

        return exp.isEmpty() || exp.startsWith("/") ? "1" + exp : exp;
    }
}
