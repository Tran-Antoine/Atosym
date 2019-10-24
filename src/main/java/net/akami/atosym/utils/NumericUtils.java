package net.akami.atosym.utils;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;

import java.math.BigDecimal;
import java.math.MathContext;

public class NumericUtils {

    private NumericUtils(){}

    public static float sum(float a, float b, MaskContext context) {
        return operation(a, b, context, BigDecimal::add);
    }

    public static float mult(float a, float b, MaskContext context) {
        return operation(a, b, context, BigDecimal::multiply);
    }

    public static float div(float a, float b, MaskContext context) {
        return operation(a, b, context, BigDecimal::divide);
    }

    public static float pow(float a, float b, MaskContext context) {
        if(b % 1 != 0) throw new UnsupportedOperationException("Non integer exponents aren't supported yet");

        int bInt = (int) b;
        BigDecimal bigA = new BigDecimal(a, context.getMathContext());
        return bigA.pow(bInt).floatValue();
    }

    private static float operation(float a, float b, MaskContext context, BigDecimalAction func) {
        MathContext mathContext = context.getMathContext();
        BigDecimal bigA = new BigDecimal(a, mathContext);
        BigDecimal bigB = new BigDecimal(b, mathContext);
        return func.apply(bigA, bigB, context.getMathContext()).floatValue();
    }

    public static boolean isZero(MathObject object) {
        return isANumber(object) && ((NumberExpression) object).getValue() == 0;
    }

    public static boolean isNotZero(MathObject object) {
        return !isZero(object);
    }

    private static boolean isOne(MathObject object) {
        return object.equals(MathObject.NEUTRAL_MULT);
    }

    public static boolean isNotOne(MathObject object) {
        return !isOne(object);
    }

    public static boolean isANumber(MathObject object) {
        return object.getType() == MathObjectType.NUMBER;
    }

    public static boolean isAnInteger(MathObject object) {
        return isANumber(object) && ((NumberExpression) object).getValue() % 1 == 0;
    }

    private interface BigDecimalAction {
        BigDecimal apply(BigDecimal a, BigDecimal b, MathContext mode);
    }
}
