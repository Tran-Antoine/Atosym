package net.akami.mask.utils;

import net.akami.mask.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


// TODO : add more temporary variables
public class MathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);

    public static String sum(String a, String b) {
        return Adder.getInstance().rawOperate(a, b);
    }
    public static String sum(List<String> monomials) {
        return Adder.getInstance().monomialSum(monomials, true);
    }
    public static String subtract(String a, String b) {
        return Subtractor.getInstance().rawOperate(a, b);
    }
    public static String mult(String a, String b) {
        return Multiplicator.getInstance().rawOperate(a, b);
    }
    public static String divide(String a, String b) {
        return Divider.getInstance().rawOperate(a, b);
    }
    public static String diffSum(String a, String altA, String b, String altB) {
        return sum(altA, altB);
    }
    public static String diffSubtract(String a, String altA, String b, String altB) {
        return subtract(altA, altB);
    }
    public static String diffMult(String a, String altA, String b, String altB) {
        return sum(mult(altA, b), mult(altB, a));
    }
    public static String diffDivide(String a, String altA, String b, String altB) {
        return "("+subtract(mult(altA, b), mult(altB, a))+")/("+b+")^2";
    }
    public static String diffPow(String a, String altA, String b, String altB) {
        String subtractResult = subtract(b, "1");
        if(subtractResult.equals("1"))
            subtractResult = "";
        else if(!ExpressionUtils.isReduced(subtractResult))
            subtractResult = "^("+subtractResult+')';
        else
            subtractResult = '^'+subtractResult;

        if(!ExpressionUtils.isReduced(a) && !ExpressionUtils.areEdgesBracketsConnected(a, false))
            a = '(' + a + ')';
        if(!ExpressionUtils.isReduced(b) && !ExpressionUtils.areEdgesBracketsConnected(b, false))
            b = '(' + b + ")*";
        else
            b += '*';

        if(altA.equals("1"))
            altA = "";
        else if(!ExpressionUtils.isReduced(altA))
            altA = '(' + altA + ')';
        else
            altA = '*' + altA;

        return b+a+subtractResult+altA;
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
        return PowCalculator.getInstance().rawOperate(a, b);
    }

    public static String sin(String a) {
        return trigonometryOperation(a, '@', Math::sin);
    }

    public static String cos(String a) {
        return trigonometryOperation(a, '#', Math::cos);
    }

    public static String tan(String a) {
        return trigonometryOperation(a, '§', Math::tan);
    }

    public static String trigonometryOperation(String a, char opChar, UnaryOperation operation) {
        if(ExpressionUtils.isANumber(a)) {
            double result = operation.compute(Double.valueOf(a));
            return String.valueOf(result > 10E-15 ? result : 0);
        }
        return "("+a+")"+opChar;
    }

    /*public static String highPow(String a, String b) {
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
            LOGGER.info("PowCalculator value contains variables or pow value is greater than 9. Returns a^b");
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
    }*/

    public static String roundPeriodicSeries(String value) {
        if(!value.contains(".")) return value;

        String[] parts = value.split("\\.");
        if(parts[1].matches("[9]+") && parts[1].length() > 15) {
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

    /**
     * The reason why it returns an array of strings instead of an array of integers is that
     * it is easier to deal with strings during the reduction process
     * @param self the number to decompose
     * @return a list of strings being the divided version of the given parameter
     */
    public static List<String> decomposeNumber(float self) {
        LOGGER.info("Now decomposing float {}", self);

        List<String> results = new ArrayList<>();
        if (self % 1 != 0) {
            LOGGER.info("Non-integer given, returns it");
            results.add(String.valueOf(self));
            return results;
        }
        if (self < 0) {
            results.add("-1");
            self *= -1;
        }

        List<Integer> dividers = getDividers(self);

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

    public static List<Integer> getDividers(float self) {
        List<Integer> dividers = new ArrayList<>();
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
        return dividers;
    }

    @FunctionalInterface
    private interface UnaryOperation {
        double compute(double d);
    }
}
