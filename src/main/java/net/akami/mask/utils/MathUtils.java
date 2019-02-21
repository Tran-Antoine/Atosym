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
    private static final String DELETE_NON_VARIABLES = "[\\d.+\\-]+";

    //TODO remove spaces
    public static String sum(String a, String b) {

        LOGGER.debug("Sum process : \n");
        List<String> monomials = toMonomials(a);
        monomials.addAll(toMonomials(b));

        return sum(monomials);
    }

    public static String sum(List<String> monomials) {

        List<String> finalMonomials = new ArrayList<>();

        for(int i = 0; i < monomials.size(); i++) {
            String part = monomials.get(i);
            if(part == null)
                continue;

            String vars = toVariables(part);
            Map<BigDecimal, Integer> compatibleParts = new HashMap<>();

            for(int j = 0; j < monomials.size(); j++) {
                if(i == j)
                    continue;

                String part2 = monomials.get(j);
                if(part2 == null)
                    continue;

                if(toVariables(part2).equals(vars)) {
                    compatibleParts.put(new BigDecimal(numericValueOf(part2)), j);
                }
            }
            BigDecimal finalTotal = new BigDecimal(numericValueOf(part));

            for(BigDecimal value : compatibleParts.keySet()) {
                finalTotal = finalTotal.add(value);
                monomials.set(compatibleParts.get(value), null);
            }
            monomials.set(i, null);
            finalMonomials.add(cutSignificantZero(finalTotal.toString())+vars);
        }

        finalMonomials.addAll(monomials);

        clearBuilder();
        for(String rest : finalMonomials) {
            LOGGER.debug("Part : "+rest);
            if(rest == null)
                continue;

            if(rest.startsWith("+") || rest.startsWith("-")) {
                BUILDER.append(rest);
            } else {
                BUILDER.append("+"+rest);
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
        for(int i = 0; i < bMonomials.size(); i++) {
            String m = bMonomials.get(i);
            if(!m.startsWith("-")) {
                if(m.startsWith("+")) {
                    bMonomials.set(i, "-"+m.substring(1));
                } else {
                    bMonomials.set(i, "-"+m);
                }
            }
        }
        monomials.addAll(bMonomials);
        return sum(monomials);
    }

    public static String mult(String a, String b) {
        LOGGER.debug("Operation process : \n");

        List<String> aMonomials = toMonomials(a);
        List<String> bMonomials = toMonomials(b);

        // We can't use the constant BUILDER, because it is cleared repeatedly inside the loop
        StringBuilder builder = new StringBuilder();

        for (String part : aMonomials) {
            for (String part2 : bMonomials) {
                String result = simpleMult(part, part2);
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

    public static List<String> toMonomials(String self) {

        List<String> monomials = new ArrayList<>();
        int lastIndex = 0;
        for (int i = 0; i < self.length(); i++) {
            // We don't want i = 0 because if the first char is '-', it will add an empty string to the list
            if (self.charAt(i) == '+' || self.charAt(i) == '-' && i != 0) {
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

    private static String simpleMult(String a, String b) {
        String originalVars = toVariables(a + b);
        char[] varsArray = originalVars.toCharArray();
        clearBuilder();

        for (int i = 0; i < varsArray.length; i++) {
            int count = 0;
            char c1 = varsArray[i];
            if (c1 == ' ')
                continue;

            for (int j = 0; j < varsArray.length; j++) {
                char c2 = varsArray[j];

                if (c1 == c2) {
                    count++;
                    varsArray[i] = ' ';
                    varsArray[j] = ' ';
                }
            }
            String result = count == 1 ? String.valueOf(c1) : c1 + "^" + count;
            BUILDER.append(result);
        }
        BigDecimal aValue = new BigDecimal(numericValueOf(a));
        BigDecimal bValue = new BigDecimal(numericValueOf(b));
        String floatResult = cutSignificantZero(aValue.multiply(bValue).toString());
        return floatResult + BUILDER.toString();
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
        LOGGER.debug("Operation process : \n");

        String vars = toVariables(a + b);

        if (vars.length() == 0) {
            return String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
        }
        return a + "^" + b;
    }

    public static String toVariables(String exp) {
        char[] chars = exp.replaceAll(DELETE_NON_VARIABLES, "").toCharArray();
        Arrays.sort(chars);
        clearBuilder();

        for(char c : chars)
            BUILDER.append(c);
        LOGGER.debug("Variables found : {}", BUILDER.toString());
        return BUILDER.toString();
    }

    private static String cutSignificantZero(String self) {
        return self.endsWith(".0") ? self.substring(0, self.length() - 2) : self;
    }

    private static String numericValueOf(String self) {
        String numericValue = self.replaceAll(DELETE_VARIABLES, "");
        String finalNumericValue = numericValue.isEmpty() ? "1" : numericValue;
        if(finalNumericValue.equals("-")) {
            finalNumericValue = "-1";
        }
        LOGGER.debug("Numeric value of {} : {}", self, finalNumericValue);
        return finalNumericValue;
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
