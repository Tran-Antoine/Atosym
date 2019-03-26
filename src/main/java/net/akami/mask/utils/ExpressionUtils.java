package net.akami.mask.utils;

import net.akami.mask.handler.sign.BinaryOperationSign;
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

        while (ExpressionUtils.areEdgesBracketsConnected(self, true)) {
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
                if (isReduced(exponent) || ExpressionUtils.areEdgesBracketsConnected(exponent, true)) {
                    BUILDER.append(exponent);
                } else {
                    BUILDER.append('(').append(exponent).append(')');
                }
            }
        }
        return BUILDER.toString();
    }

    public static boolean isTrigonometric(String exp) {
        return exp.contains("@") || exp.contains("#") || exp.contains("§");
    }

    public static boolean isTrigonometricShortcut(String exp) {
        return exp.length() == 1 && TRIGONOMETRY_SHORTCUTS.contains(exp);
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
        result = FormatterFactory.addMultiplicationSigns(result, false);

        String[] varsResult = result.split("\\*");
        // Replaces x by x^1
        for (int i = 0; i < varsResult.length; i++) {
            String actual = varsResult[i];
            if (!actual.isEmpty() && !actual.contains("^"))
                varsResult[i] = actual + "^" + 1;
        }

        return varsResult;
    }

    public static boolean isReduced(String self) {
        return !(self.contains("+") || self.contains("-") || self.contains("*") || self.contains("/")
                || self.contains("^"));
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
            String pow = monomial.split("\\^", 2)[1];
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
        elements.addAll(Arrays.asList(FormatterFactory.addMultiplicationSigns(exp, false).split("\\*")));
        List<String> decomposedElements = new ArrayList<>();
        // We can't use a classic for each loop since the size of the list will be modified
        for (int i = 0; i < elements.size(); i++) {
            String element = elements.get(i);
            if (isANumber(element)) {
                List<String> decomposedLocal = MathUtils.decomposeNumber(Float.parseFloat(element));
                LOGGER.info("Decomposed {}, result : {}", element, decomposedLocal);
                elements.set(i, null);
                decomposedElements.addAll(decomposedLocal);
            } else if (!isTrigonometric(element) && element.length() > 2 && element.charAt(1) == '^') {
                decomposePoweredVariable(element, i, elements, decomposedElements);
            }
        }
        elements.addAll(decomposedElements);
        elements = elements.stream().filter(x -> x != null && !x.equals("1.0")).collect(Collectors.toList());
        LOGGER.debug("Elements of {} : {}", exp, elements);
        return elements;
    }

    private static void decomposePoweredVariable(String origin, int i, List<String> in, List<String> out) {
        String exponent = origin.substring(2);
        while (ExpressionUtils.areEdgesBracketsConnected(exponent, true))
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

    public static boolean isANumber(String exp) {
        if (exp.length() == 0)
            return false;
        return exp.substring(1).matches("[\\d]+(.[\\d]+|)") || NUMBERS.contains(exp);
    }

    public static boolean isSigned(String exp) {
        return exp.charAt(0) == '+' || exp.charAt(0) == '-';
    }

    public static boolean areEdgesBracketsConnected(String exp, boolean falseIfTrigonometry) {
        if (exp.isEmpty() || exp.charAt(0) != '(') {
            return false;
        }

        if(exp.length() > 1 &&
                ExpressionUtils.isTrigonometric(String.valueOf(exp.charAt(exp.length()-2))) && falseIfTrigonometry) {
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
            LOGGER.info("Character at index {} in {} is right after * or /. Is surrounded = true", index, exp);
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

    public static String toNumericValue(String exp) {
        if (exp.isEmpty())
            return "0";

        exp = FormatterFactory.addAllCoefficients(FormatterFactory.removeMultiplicationSigns(exp))
                // deletes trigonometry stuff
                .replaceAll("\\(\\(.+\\)([@#§])\\)"+"(\\^([0-9.]+|((1|)[a-zA-DF-Z]))|)", "")
                .replaceAll("\\(.+\\)([@#§])"+"(\\^([0-9.]+|((1|)[a-zA-DF-Z]))|)", "")
                // deletes variables + their pow value
                .replaceAll("[a-zA-DF-Z]\\^(([a-zA-DF-Z0-9^]+)|\\((.+\\)))", "")
                .replaceAll("(\\*[a-zA-DF-Z])|[a-zA-DF-Z]", "")
                // deletes spaces
                .replaceAll("\\s", "");

        if (exp.equals("-") || exp.equals("+") || exp.isEmpty()) {
            exp = exp + "1";
        }

        if(exp.contains("/")) {
            String[] parts = exp.split("/", 2);
            return MathUtils.divide(parts[0], parts[1]);
        }

        return exp;
    }

    public static String removeEdgeBrackets(String exp, boolean falseIfTrigonometry) {
        while(areEdgesBracketsConnected(exp, falseIfTrigonometry))
            exp = exp.substring(1, exp.length()-1);
        return exp;
    }

    public static boolean hasHigherPriority(String a, String b) {
        return priorityLevelFor(a) > priorityLevelFor(b);
    }

    private static int priorityLevelFor(String self) {
        int level = 3;

        for(char c : self.toCharArray()) {
            BinaryOperationSign sign = BinaryOperationSign.getBySign(c);
            if(sign == null) continue;

            int cLevel = sign.getPriorityLevel();

            if(level > cLevel)
                level = cLevel;
        }
        return level;
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
