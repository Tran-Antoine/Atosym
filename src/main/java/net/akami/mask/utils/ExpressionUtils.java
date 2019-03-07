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
    private static final String DELETE_NON_VARIABLES = "[\\d.+\\-/*()^]+";
    public static final String MATH_SIGNS = "+-*/^()";
    public static final String NUMBERS = "0123456789";
    // 'E' deliberately missing
    public static final String VARIABLES = "abcdefghijklmnopqrstuvwxyzABCDFGHIJKLMNOPQRSTUVWXYZ";
    public static final String TRIGONOMETRY_SHORTCUTS = "@#§";
    public static List<String> toMonomials(String self) {

        while(ExpressionUtils.areEdgesBracketsConnected(self)) {
            self = self.substring(1, self.length()-1);
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
                if(toNumericValue(monomial).equals("0")) continue;

                monomials.add(monomial);
                LOGGER.debug("Added {} to monomials because sign found", monomial);
                lastIndex = i;
            }
            if (i == self.length() - 1) {
                String monomial = self.substring(lastIndex, i + 1);
                if(toNumericValue(monomial).equals("0")) continue;

                monomials.add(monomial);
                LOGGER.debug("Added {} to monomials because we reached the end of the String", monomial);
            }
        }
        return monomials;
    }

    public static String toVariablesUnused(String exp) {

        String[] unSortedVars = keepEachCharacterOnce(exp.replaceAll(DELETE_NON_VARIABLES, "")).split("");
        if (unSortedVars.length == 0)
            return "";

        Arrays.sort(unSortedVars);
        String vars = String.join("", unSortedVars);

        LOGGER.debug("Looking for the variables of the sequence : {}", exp);
        List<String> finalVars = new ArrayList<>();
        StringBuilder builtExpression = new StringBuilder();
        builtExpression.append(exp);
        clearBuilder();
        // Going through the variables
        for (int i = 0; i < vars.length(); i++) {
            int partsAmount = 0;
            char var = vars.charAt(i);

            LOGGER.debug("Treating {}", var);

            // can happen if 'x^y', the y has been transformed to '$', meaning it wasn't a variable but an sequence.
            if (!builtExpression.toString().contains(String.valueOf(var))) {
                LOGGER.debug("Variable {} not found. Skipping.", var);
                continue;
            }

            for (int j = 0; j < builtExpression.length(); j++) {

                char c1 = builtExpression.charAt(j);
                // The treated variable has been found
                if (var == c1) {
                    partsAmount++;
                    LOGGER.debug("Identical variable found at index {} : {}", j, c1);
                    // If now powers after the variable
                    if (j + 2 > builtExpression.length() || builtExpression.charAt(j + 1) != '^') {
                        BUILDER.append("+1");
                        LOGGER.debug("No power found. Adds 1");
                    } else {
                        SequenceCalculationResult sequenceResult = groupAfter(j + 1, exp);
                        String exponent = sequenceResult.sequence;
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
                LOGGER.debug("Several parts forming the power : {}. Needs extra monomialSum", BUILDER.toString());
                String reducedExponent = MathUtils.sum(BUILDER.toString(), "");
                clearBuilder();
                BUILDER.append(reducedExponent);
            }
            String exponent = BUILDER.toString();
            if (!isReduced(exponent)) {
                exponent = "(" + exponent + ")";
            }
            LOGGER.debug("Final sequence calculation of {} : {}", var, exponent);
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

    public static String toVariables(String exp) {
        Pattern pattern = Pattern.compile("(([a-zA-DF-Z]\\^(\\(.+\\)|([0-9.]+)|[a-zA-DF-Z]))|[a-zA-DF-Z])+");
        Matcher matcher = pattern.matcher(exp);
        StringBuilder builder = new StringBuilder();
        while(matcher.find())
            builder.append(matcher.group());
        String result = builder.toString();
        builder.delete(0, builder.length());
        Map<Character, String> variables = new HashMap<>();
        for(char var : toVariablesType(result).toCharArray()) {
            variables.put(var, "0");
        }
        for(int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if(i!=0 && VARIABLES.contains(String.valueOf(c))) {
                String before = String.valueOf(result.charAt(i-1));
                if((NUMBERS.contains(before) || VARIABLES.contains(before)) && !before.equals("1")) {
                    builder.append("*").append(c);
                } else {
                    builder.append(c);
                }
            } else {
                builder.append(c);
            }
        }
        result = builder.toString();
        LOGGER.debug("After rewriting result from {} : {}", exp, result);
        builder.delete(0, builder.length());

        for(int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if(variables.containsKey(c)) {
                LOGGER.debug("Char {} at index {} in {} is a var", c, i, result);
                if(i == result.length()-1 || result.charAt(i+1) != '^') {
                    String sumResult = MathUtils.sum(variables.get(c), "1");
                    sumResult = sumResult.matches("[\\da-zA-Z.]+") ? sumResult : "(" + sumResult + ")";
                    LOGGER.debug("Sum result of {} and 1 : {}", variables.get(c), sumResult);
                    variables.put(c, sumResult);
                    LOGGER.debug("Exponent of {} is now {}", c, variables.get(c));
                } else {
                    String cutPart = result.substring(i+2);
                    Pattern cutPattern = Pattern.compile("([a-zA-DF-Z0-9]+)|\\(.+\\)");
                    Matcher cutMatcher = cutPattern.matcher(cutPart);
                    String group = null;
                    if(cutMatcher.find())
                        group = cutMatcher.group();

                    LOGGER.debug("Group after {} is : {}", c, group);
                    LOGGER.debug("--> Result was previously {}", result);
                    result = result.replaceFirst(Pattern.quote(group), group.replaceAll(".", "_"));
                    LOGGER.debug("--> Result is now : {}", result);
                    String sumResult = MathUtils.sum(variables.get(c), group);
                    sumResult = sumResult.matches("[\\da-zA-Z.]+") ? sumResult : "(" + sumResult + ")";
                    variables.put(c, sumResult);
                    LOGGER.debug("Variables state : {}", variables);
                }
            }
        }
        for(Character var : variables.keySet()) {
            if(variables.get(var).equals("0"))
                continue;
            if(variables.get(var).equals("1")) {
                builder.append(var);
            } else {
                builder.append(var).append("^").append(variables.get(var));
            }
        }
        return builder.toString();
    }

    /**
     * groupAfter(1, "3^(4+x)") will return 3 and 6 as start and end
     * @param index
     * @param exp
     * @return
     */
    public static SequenceCalculationResult groupAfter(int index, String exp) {
        if(index < exp.length() -1 && exp.charAt(index+1) == '(') {
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

            if(i == exp.length() -1) {
                LOGGER.debug("Reached the end of the string.");
                result.sequence = exp.substring(index+1);
                result.start = index+1;
                result.end = exp.length();
            }
        }
        //removeBrackets(result, exp, index, true);
        LOGGER.debug("Result of group at index {} for {} : {}", index, exp, result.sequence);
        return result;
    }
    public static SequenceCalculationResult groupBefore(int index, String exp) {
        if(index > 0 && exp.charAt(index-1) == ')') {
            return bracketSequenceBefore(index, exp);
        }
        LOGGER.debug("No bracket sequence after char at index {}", index);
        SequenceCalculationResult result = new SequenceCalculationResult();
        for (int i = index - 1; i >= 0; i--) {
            char c = exp.charAt(i);

            if ("+-*/^".contains(String.valueOf(c))) {
                LOGGER.debug("Found an operator : {} at index {}. Stopped", c, i);
                result.sequence = exp.substring(i, index);
                result.start = i;
                result.end = index;
                break;
            }

            if(i == 0) {
                LOGGER.debug("Reached the end of the string.");
                result.sequence = exp.substring(0, index);
                result.start = 0;
                result.end = index;
            }
        }
        removeBrackets(result, exp, index, false);
        LOGGER.debug("Result of group at index {} for {} : {}", index, exp, result.sequence);
        return result;
    }

    public static SequenceCalculationResult bracketSequenceAfter(int index, String exp) {

        SequenceCalculationResult result = new SequenceCalculationResult();

        int openingBracketsCount = 0;
        for (int i = index + 1; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (c == ')') {
                if(openingBracketsCount <= 0) {
                    LOGGER.debug("Found the closing bracket");
                    result.start = index + 2;
                    result.sequence = exp.substring(index + 2, i);
                    result.end = i;
                    break;
                } else {
                    openingBracketsCount--;
                }
            }
            if(c == '(' && i != index+1) {
                openingBracketsCount++;
            }
        }

        //removeBrackets(result, exp, index, true);
        LOGGER.debug("Sequence {} after pow at index {} found : {}", exp, index, result.sequence);
        return result;
    }

    public static SequenceCalculationResult bracketSequenceBefore(int index, String exp) {
        SequenceCalculationResult result = new SequenceCalculationResult();

        int closingBrackets = 0;
        for (int i = index - 1; i >= 0; i--) {
            char c = exp.charAt(i);

            if (c == '(') {
                if(closingBrackets <= 0) {
                    LOGGER.debug("Found the closing bracket");
                    result.start = i+1;
                    result.sequence = exp.substring(i+1, index-1);
                    result.end = index-1;
                    break;
                } else {
                    closingBrackets--;
                }
            }
            if(c == ')' && i != index+1) {
                closingBrackets++;
            }
        }

        //removeBrackets(result, exp, index, false);
        LOGGER.debug("Sequence {} after pow at index {} found : {}", exp, index, result.sequence);
        return result;
    }

    private static void removeBrackets(SequenceCalculationResult result, String exp, int index, boolean leftToR) {
        if (result.sequence == null) {
            LOGGER.debug("Sequence null");
            result.sequence = leftToR ? exp.substring(index + 1) : exp.substring(0, index);
            result.start = leftToR ? index : 0;
            result.end = leftToR ? exp.length() : index;
        }
        while(areEdgesBracketsConnected(result.sequence)) {
            result.sequence = result.sequence.substring(1, result.sequence.length()-1);
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
            boolean varOrTrigo = ExpressionUtils.VARIABLES.contains(c) || ExpressionUtils.TRIGONOMETRY_SHORTCUTS.contains(c);

            if(varOrTrigo && i!= 0 && !ExpressionUtils.MATH_SIGNS.contains(String.valueOf(self.charAt(i-1)))) {
                    BUILDER.append("*").append(c);
            } else if(i != 0 && c.equals("(") &&
                    (self.charAt(i-1) == ')' || !MATH_SIGNS.contains(String.valueOf(self.charAt(i-1))))) {
                BUILDER.append("*").append(c);
            } else if(i!= 0 && self.charAt(i-1) == ')' && !MATH_SIGNS.contains(String.valueOf(c))) {
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

    public static String toVariablesType(List<EquationSolver.BiMask> biMasks) {
        clearBuilder();
        for(EquationSolver.BiMask biMask : biMasks) {
            String line = biMask.getLeft().getExpression() + biMask.getRight().getExpression();
            BUILDER.append(line);
        }
        return toVariablesType(BUILDER.toString());
    }
    public static String toVariablesType(String self) {
        String letters = self.replaceAll("[\\d.+\\-*/^()]+", "");
        Set<String> chars = new HashSet<>();
        for(char c : letters.toCharArray()) {
            chars.add(String.valueOf(c));
        }
        return String.join("", chars);
    }

    /**
     *
     * @param exp an already reduced expression
     * @return
     */
    public static int getMaximalNumericPower(String exp) {
        List<String> monomials = toMonomials(exp);
        int highest = 0;

        for(String monomial : monomials) {
            if(!monomial.contains("^")) {
                highest = highest < 1 ? 1 : highest;
                continue;
            }
            String pow = groupAfter(monomial.indexOf('^'), monomial).sequence;
            if(pow.matches("[\\d+-]")) {
                int value = Integer.parseInt(pow.replaceAll("\\s", ""));
                if(value > highest) {
                    highest = value;
                }
            }
        }
        return highest;
    }
    // TODO : support (x+1) denominator for instance
    /**
     *
     * @param exp an already reduced expression
     * @return
     */
    public static List<String> decompose(String exp) {
        LOGGER.info("Now decomposing expression {}", exp);
        List<String> elements = new ArrayList<>();
        elements.addAll(Arrays.asList(cancelMultShortcut(exp).split("\\*")));
        List<String> decomposedElements = new ArrayList<>();
        // We can't use a classic for each loop since the size of the list will be modified
        for(int i = 0; i < elements.size(); i++) {
            String element = elements.get(i);
            if(isANumber(element)) {
                List<String> decomposedLocal = decomposeNumber(Float.parseFloat(element));
                LOGGER.info("Decomposed {}, result : {}", element, decomposedLocal);
                elements.set(i, null);
                decomposedElements.addAll(decomposedLocal);
            } else {
                LOGGER.debug("{} is not a number", element);
                if(element.length() > 2 && element.charAt(1) == '^') {
                    decomposePoweredVariable(element, i, elements, decomposedElements);
                }
            }
        }
        elements.addAll(decomposedElements);
        elements = elements.stream().filter(x -> x!= null && !x.equals("1.0")).collect(Collectors.toList());
        LOGGER.debug("Elements of {} : {}", exp, elements);
        return elements;
    }

    private static void decomposePoweredVariable(String origin, int i, List<String> in, List<String> out) {
        String exponent = origin.substring(2);
        while(ExpressionUtils.areEdgesBracketsConnected(exponent))
            exponent = exponent.substring(1, exponent.length()-1);
        if(ExpressionUtils.isANumber(exponent)) {
            float exponentValue = Float.parseFloat(exponent);
            if(exponentValue % 1 != 0)
                return;

            in.set(i, null);
            for(int j = 0; j < exponentValue; j++) {
                out.add(String.valueOf(origin.charAt(0)));
            }
        }
    }
    public static List<String> decomposeNumber(float self) {
        LOGGER.info("Now decomposing float {}", self);

        List<String> results = new ArrayList<>();
        if(self % 1 != 0) {
            LOGGER.info("Non-integer given, returns it");
            results.add(String.valueOf(self));
            return results;
        }
        List<Integer> dividers = new ArrayList<>();
        if(self < 0) {
            results.add("-1");
            self *= -1;
        }
        // Builds the dividers array
        for(int i = 2; i <= self; i++) {
            boolean unique = true;
            for(int j = 2; j < i; j++) {
                if(i % j == 0 && (j != i && i!=2)) {
                    unique = false;
                    break;
                }
            }
            if(unique) {
                dividers.add(i);
                continue;
            }
        }
        int index = 0;
        while(index < dividers.size()) {
            int divider = dividers.get(index);
            if(self % divider == 0) {
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
        if(exp.length() == 0)
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
        if(index > 0 && (exp.charAt(index-1) == '/' || exp.charAt(index-1) == '*')) {
            LOGGER.info("Character right after * or /. Is surrounded = true");
            return true;
        }

        int leftParenthesis = 0;

        for(int i = 0; i < exp.length(); i++) {
            if(exp.charAt(i) == '(') {
                leftParenthesis++;
            }

            if(exp.charAt(i) == ')') {
                leftParenthesis--;
            }
            if(leftParenthesis > 0 && i == index) {
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
        if(exp.isEmpty())
            return "0";
        exp = ExpressionUtils.addMultShortcut(exp)
                .replaceAll("\\(.+\\)([@#§])", "")
                .replaceAll("[a-zA-DF-Z]\\^(([a-zA-DF-Z0-9^]+)|\\((.+\\)))", "")
                .replaceAll("(\\*[a-zA-DF-Z])|[a-zA-DF-Z]", "")
                .replaceAll("\\s", "");

        if(exp.equals("-") || exp.equals("+")) {
            exp = exp + "1";
        }
        return exp.isEmpty() || exp.startsWith("/") ? "1"+exp : exp;
    }
}
