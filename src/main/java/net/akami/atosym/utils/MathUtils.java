package net.akami.atosym.utils;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.akami.atosym.core.MaskContext.DEFAULT;


public class MathUtils {

    // private static final Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);

    public static MathObject sum(MathObject a, MathObject b)      { return sum(a, b, DEFAULT);      }
    public static MathObject subtract(MathObject a, MathObject b) { return subtract(a, b, DEFAULT); }
    public static MathObject mult(MathObject a, MathObject b)     { return mult(a, b, DEFAULT);     }
    public static MathObject divide(MathObject a, MathObject b)   { return divide(a, b, DEFAULT);   }
    public static MathObject pow(MathObject a, MathObject b)      { return pow(a, b, DEFAULT);      }

    public static MathObject sum(MathObject a, MathObject b, MaskContext context)      { return context.binaryCompute(a, b, SumOperator.class);         }
    public static MathObject subtract(MathObject a, MathObject b, MaskContext context) { return context.binaryCompute(a, b, SubOperator.class);    }
    public static MathObject mult(MathObject a, MathObject b, MaskContext context)     { return context.binaryCompute(a, b, MultOperator.class); }
    public static MathObject divide(MathObject a, MathObject b, MaskContext context)   { return context.binaryCompute(a, b, DivOperator.class);       }
    public static MathObject pow(MathObject a, MathObject b, MaskContext context)      { return context.binaryCompute(a, b, PowerOperator.class); }

    public static MathObject diffSum(MathObject a, MathObject altA, MathObject b, MathObject altB) {
        return sum(altA, altB);
    }
    public static MathObject diffSubtract(MathObject a, MathObject altA, MathObject b, MathObject altB) {
        return subtract(altA, altB);
    }
    public static MathObject diffMult(MathObject a, MathObject altA, MathObject b, MathObject altB) {
        return sum(mult(altA, b), mult(altB, a));
    }
    public static MathObject diffDivide(MathObject a, MathObject altA, MathObject b, MathObject altB) {
        return divide(subtract(mult(altA,b), mult(altB, a)), mult(b, b));
    }
    public static MathObject diffPow(MathObject a, MathObject altA, MathObject b, MathObject altB) {
        return mult(mult(b, pow(a, subtract(b, new NumberExpression(1f)))), altA);
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
            return String.valueOf(Math.abs(result) > 10E-15 ? result : 0);
        }
        /* TODO: Replace with
            DisplayUtils.surroundWithParenthesis(DisplayUtils.surroundWithParenthesis(a) + opChar);
            or
            DisplayUtils.surroundWithParenthesis("(" + a + ")" + opChar);
            or not??
         */
        return "(("+a+")"+opChar+")";
    }

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
    /*public static List<String> decomposeNumberToString(float self) {
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

    public static List<Float> decomposeNumber(float self) {
        return decomposeNumber(self, 0);
    }

    public static List<Float> decomposeNumber(float self, float other) {
        LOGGER.info("Now decomposing float {}", self);

        List<Float> results = new ArrayList<>();
        if(other != 0 && self % other == 0) {
            float divResult = self / other;
            self /= divResult;
            results.add(divResult);
        }

        if (self % 1 != 0) {
            LOGGER.info("Non-integer given, returns it");
            results.add(self);
            return results;
        }
        if (self < 0) {
            results.add(-1.0f);
            self *= -1;
        }

        List<Integer> dividers = getDividers(self);

        int index = 0;
        while (index < dividers.size()) {
            int divider = dividers.get(index);
            if (self % divider == 0) {
                LOGGER.debug("{} is a divider of {}", divider, self);
                results.add((float) divider);
                self /= divider;
            } else {
                LOGGER.debug("{} is not a divider of {}", divider, self);
                index++;
            }
        }
        LOGGER.info("Result : {} (decomposition of {})", results, self);
        return results;
    }

    public static List<Integer> getDividers(float self) {
        List<Integer> dividers = new ArrayList<>();
        // Builds the dividers array
        for (int i = 2; i <= self; i++) {
            boolean unique = true;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                dividers.add(i);
            }
        }
        return dividers;
    }

    public static List<Variable> decomposeVariables(Iterable<Variable> vars) {

        List<Variable> finalVars = new ArrayList<>();

        for(Variable var : vars) {
            if(var instanceof SingleCharVariable) {finalVars.add(var); continue; }

            IntricateVariable complexVar = (IntricateVariable) var;
            if(complexVar.getOverlaysSize() == 0) {finalVars.add((complexVar)); continue; }

            ExpressionOverlay last = complexVar.getOverlay(-1);

            if(!(last instanceof ExponentOverlay)) {finalVars.add((complexVar)); continue; }

            ExponentOverlay exponent = (ExponentOverlay) last;

            if(ExpressionUtils.isANumber(exponent)) {
                float expValue = exponent.get(0).getNumericValue();
                List<ExpressionOverlay> finalOverlays = new ArrayList<>(complexVar.getOverlaysSection(0, -2));
                finalOverlays.add(ExponentOverlay.NULL_FACTOR);

                while (expValue > 1) {
                    finalVars.add(new IntricateVariable(complexVar.getElements(), finalOverlays));
                    expValue--;
                }

                if (expValue != 0) {
                    List<ExpressionOverlay> otherFinalOverlays = new ArrayList<>(complexVar.getOverlaysSection(0, -2));
                    otherFinalOverlays.add(ExponentOverlay.fromExpression(Expression.of(expValue)));
                    finalVars.add(new IntricateVariable(complexVar.getElements(), otherFinalOverlays));
                }
            } else {
                finalVars.add(complexVar);
            }
        }
        return finalVars;
    }
    */
    @FunctionalInterface
    private interface UnaryOperation {
        double compute(double d);
    }
}
