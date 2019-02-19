package net.akami.mask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class MathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);
    public static String sum(String a, String b) {
        if(getVariables(a+b).length() == 0) {
            String result = String.valueOf(Float.parseFloat(a) + Float.parseFloat(b));
            return result.endsWith(".0") ? result.substring(0, result.length()-2) : result;
        }
        return a + "+" + b;
    }
    public static String subtract(String a, String b) {
        if(getVariables(a+b).length() == 0) {
            BigDecimal bigA = new BigDecimal(a);
            BigDecimal bigB = new BigDecimal(b);
            String result = bigA.subtract(bigB).toString();
            return result.endsWith(".0") ? result.substring(0, result.length()-2) : result;
        }
        return a + "-" + b;
    }
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
        float aValue = Float.parseFloat(a.replaceAll("[a-zA-Z]+", ""));
        float bValue = Float.parseFloat(b.replaceAll("[a-zA-Z]+", ""));

        String floatResult = String.valueOf(aValue * bValue);
        floatResult = floatResult.endsWith(".0") ? floatResult.substring(0, floatResult.length()-2) : floatResult;
        return floatResult + reducedVars.toString();
    }

    public static String divide(String a, String b) {
        String vars = getVariables(a+b);

        if(vars.length() == 0) {
            BigDecimal bigA = new BigDecimal(a);
            BigDecimal bigB = new BigDecimal(b);
            String result = bigA.divide(bigB).toString();
            return result.endsWith(".0") ? result.substring(0, result.length()-2) : result;
        }
        return a + "/" + b;
    }
    public static String pow(String a, String b) {
        return a + "^" + b;
    }

    public static String getVariables(String exp) {
        String vars = exp.replaceAll("[\\d.+\\-]+", "");
        LOGGER.debug("Variables found : {}", vars);
        return vars;
    }
}
