package net.akami.mask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;

public class MathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);
    //TODO remove spaces
    public static String sum(String a, String b) {

        char[] aVars = sort(a);
        char[] bVars = sort(b);
        BigDecimal aValue = new BigDecimal(numericValueOf(a));
        BigDecimal bValue = new BigDecimal(numericValueOf(b));

        String numericResult = cutDotZero(aValue.add(bValue).toString());

        String finalResult = resultFirstLevelOperation(aVars, bVars, numericResult);
        System.out.println("FINAL RESULT : "+finalResult);
        return finalResult == null ? a + "+" + b : finalResult;
    }
    public static String subtract(String a, String b) {

        char[] aVars = sort(a);
        char[] bVars = sort(b);
        BigDecimal bigA = new BigDecimal(numericValueOf(a));
        BigDecimal bigB = new BigDecimal(numericValueOf(b));
        String numericResult = cutDotZero(bigA.subtract(bigB).toString());

        String finalResult = resultFirstLevelOperation(aVars, bVars, numericResult);
        return finalResult == null ? a + "-" + b : finalResult;
    }

    private static String resultFirstLevelOperation(char[] aVars, char[] bVars, String numericResult) {
        if(aVars.length == 0 && bVars.length == 0) {
            LOGGER.debug("No variables found, simple calculation");
            return numericResult;
        } else if(Arrays.equals(aVars, bVars)) {
            LOGGER.debug("Variables are the same");
            return join(numericResult, aVars);
        } else {
            LOGGER.debug("Different variables, no operation can be performed");
            return null;
        }
    }

    private static String join(String numericResult, char[] commonVars) {
        String valueResult = cutDotZero(String.valueOf(numericResult));
        return valueResult + Arrays.toString(commonVars).replaceAll("[\\[\\]]", "");
    }
    // TODO use BigDecimal
    public static String mult(String a, String b) {
        String originalVars = getVariables(a+b);
        StringBuilder reducedVars = new StringBuilder();
        char[] varsArray = originalVars.toCharArray();

        for(int i = 0; i < varsArray.length; i++) {
            int count = 0;
            char c1 = varsArray[i];
            if(c1 == ' ')
                continue;

            for(int j = 0; j < varsArray.length; j++) {
                char c2 = varsArray[j];

                if(c1 == c2) {
                    count++;
                    varsArray[i] = ' ';
                    varsArray[j] = ' ';
                }
            }
            String result = count == 1 ? String.valueOf(c1) : c1 + "^" + count;
            reducedVars.append(result);
        }
        BigDecimal aValue = new BigDecimal(numericValueOf(a));
        BigDecimal bValue = new BigDecimal(numericValueOf(b));

        String floatResult = cutDotZero(aValue.multiply(bValue).toString());
        return floatResult + reducedVars.toString();
    }

    public static String divide(String a, String b) {
        String vars = getVariables(a+b);

        if(vars.length() == 0) {
            BigDecimal bigA = new BigDecimal(a);
            BigDecimal bigB = new BigDecimal(b);
            String result = cutDotZero(bigA.divide(bigB).toString());
            return result;
        }
        return a + "/" + b;
    }

    public static String pow(String a, String b) {
        String vars = getVariables(a+b);

        if(vars.length() == 0) {
            return String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
        }
        return a + "^" + b;
    }

    public static String getVariables(String exp) {
        String vars = exp.replaceAll("[\\d.+\\-]+", "");
        LOGGER.debug("Variables found : {}", vars);
        return vars;
    }

    private static String cutDotZero(String self) {
        return self.endsWith(".0") ? self.substring(0, self.length()-2) : self;
    }

    private static char[] sort(String self) {
        char[] vars = getVariables(self).toCharArray();
        Arrays.sort(vars);
        return vars;
    }

    //TODO return a String
    private static String numericValueOf(String self) {
        return self.replaceAll("[a-zA-Z]+", "");
    }
}
