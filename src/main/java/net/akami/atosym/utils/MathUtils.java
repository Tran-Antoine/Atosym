package net.akami.atosym.utils;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.operator.*;

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
    public static MathObject divide(MathObject a, MathObject b, MaskContext context)   { return context.binaryCompute(a, b, DivisionOperator.class);       }
    public static MathObject pow(MathObject a, MathObject b, MaskContext context)      { return context.binaryCompute(a, b, ExponentiationOperator.class); }

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
}
