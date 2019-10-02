package net.akami.atosym.utils;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;

import java.math.BigDecimal;
import java.math.MathContext;

public class NumericUtils {

    private NumericUtils(){}

    public static float sum(float a, float b, MaskContext context) {

        MathContext mathContext = context.getMathContext();
        BigDecimal bigA = new BigDecimal(a, mathContext);
        BigDecimal bigB = new BigDecimal(b, mathContext);

        return bigA.add(bigB).floatValue();
    }

    public static boolean isZero(MathObject object) {
        return object instanceof NumberExpression && ((NumberExpression) object).getValue() == 0;
    }

    public static boolean isNotZero(MathObject object) {
        return !isZero(object);
    }
}
