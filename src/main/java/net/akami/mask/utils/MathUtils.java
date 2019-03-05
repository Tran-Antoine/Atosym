package net.akami.mask.utils;

import net.akami.mask.operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.akami.mask.utils.ExpressionUtils.SequenceCalculationResult;

// TODO : add more temporary variables
public class MathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);
    private static final StringBuilder BUILDER = new StringBuilder();

    //TODO remove spaces
    public static String sum(String a, String b) {
        return Sum.getInstance().rawOperate(a, b);
    }

    /**
     * @param monomials
     * @return the monomialSum of all monomials given
     */
    public static String sum(List<String> monomials) {
        return Sum.getInstance().monomialSum(monomials);
    }

    public static String subtract(String a, String b) {
        return Subtraction.getInstance().rawOperate(a, b);
    }

    public static String mult(String a, String b) {
        return Multiplication.getInstance().rawOperate(a, b);
    }

    // 3x^2 / 6x -> 3 * x * x / 3 * 2 * x -> x/2
    public static String divide(String a, String b) {
        return Division.getInstance().rawOperate(a, b);
    }

    public static String breakNumericalFraction(String self) {
        for(int i = 0; i < self.length(); i++) {
            if(self.charAt(i) == '/') {
                return divide(self.substring(0, i), self.substring(i+1));
            }
        }
        return self;
    }

    // TODO : optimize : use other method to chain multiplications
    public static String pow(String a, String b) {
        return Pow.getInstance().rawOperate(a, b);
    }

    public static String sin(String a) {
        if(ExpressionUtils.isANumber(a)) {
            return String.valueOf(Math.sin(Float.valueOf(a)));
        }
        return a;
    }

    public static String cos(String a) {
        if(ExpressionUtils.isANumber(a)) {
            return String.valueOf(Math.cos(Float.valueOf(a)));
        }
        return a;
    }

    public static String tan(String a) {
        if(ExpressionUtils.isANumber(a)) {
            return String.valueOf(Math.tan(Float.valueOf(a)));
        }
        return a;
    }
    /**
     * Method currently in development. Do not use
     *
     * @param a
     * @param b
     * @return
     */
    public static String highPow(String a, String b) {
        String aVars = ExpressionUtils.toVariables(a);
        String bVars = ExpressionUtils.toVariables(b);

        LOGGER.debug("aVars : {}, bVars : {}", aVars, bVars);
        if (aVars.length() == 0 && bVars.length() == 0) {
            String result = String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
            LOGGER.info("No variable found, return a^b value : {}", result);
            return result;
        }
        float powValue;

        if (bVars.length() != 0 || (powValue = Float.parseFloat(b)) > 199) {
            LOGGER.info("Pow value contains variables or pow value is greater than 9. Returns a^b");
            return a + "^" + (ExpressionUtils.isReduced(b) ? b : "(" + b + ")");
        }

        if (!a.startsWith("(") && !a.endsWith(")")) {
            a = "(" + a + ")";
        }
        StringBuilder finalResult = new StringBuilder();
        Map<String, String> calculatedValues = new HashMap<>();
        for (int i = 0; i < powValue; i++) {
            finalResult.append(a);
        }
        LOGGER.error(finalResult.toString());
        // Amount of required reductions = powValue - 1
        int start = -1;
        for (int i = 1; i < powValue; i++) {

            if (start + 1 >= finalResult.length()) {
                start = -1;
            }
            SequenceCalculationResult result1 = ExpressionUtils.groupAfter(start, finalResult.toString());
            int veryStart = start;
            start = result1.getEnd();

            LOGGER.error("Result1 : {} from {}, i = {}", result1.getSequence(), finalResult, veryStart);
            if (start + 1 >= finalResult.length()) {
                start = -1;
                finalResult.append(result1.getSequence());
                continue;
            }
            SequenceCalculationResult result2 = ExpressionUtils.groupAfter(start, finalResult.toString());
            LOGGER.error("Result2 : {} from {}, i = {}", result2.getSequence(), finalResult, start);
            start = result2.getEnd();

            String s1 = result1.getSequence();
            String s2 = result2.getSequence();
            String concatenated = s1 + ";" + s2;
            LOGGER.error("Treating {} times {}", s1, s2);
            if (calculatedValues.keySet().contains(concatenated)) {
                String foundResult = calculatedValues.get(concatenated);
                LOGGER.error("Found similar calculation. Added {}", foundResult);
                finalResult.replace(veryStart + 1, start + 1, "(" + foundResult + ")");
            } else {
                String calculatedResult = mult(s1, s2);
                LOGGER.error(finalResult.toString());
                LOGGER.error("No similar calculation found. Added {} instead of {} to {} in {}",
                        calculatedResult, veryStart + 1, start + 1, finalResult);
                finalResult.replace(veryStart + 1, start + 1, "(" + calculatedResult + ")");
                calculatedValues.put(s1 + ";" + s2, calculatedResult);
                LOGGER.error("Builder is now {}", finalResult);
                start = veryStart + calculatedResult.length() + 2;
                LOGGER.error("Next calculation will start from {}", start);
            }
        }
        return finalResult.toString();
    }

    public static String roundPeriodicSeries(String value) {
        if(!value.contains(".")) return value;

        String[] parts = value.split("\\.");
        if(parts[1].matches("[9]+")) {
            return String.valueOf(Integer.parseInt(parts[0]) + 1);
        }
        if(parts[1].matches("[0]+"))
            return parts[0];
        return value;
    }

    public static String cutSignificantZero(String self) {
        self = roundPeriodicSeries(self);
        return self.endsWith(".0") ? self.substring(0, self.length() - 2) : self;
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
