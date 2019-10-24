package net.akami.atosym.handler.sign;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.MathUtils;

import java.util.Objects;

/**
 * A QuaternaryMathOperation links a char to a defined operation, taking four strings as inputs, and computing a single
 * string out of the input. <br> <br>
 *
 * The operation is defined using method referencing, via the functional sub-interface {@link QuaternaryMathOperation}.
 * All referenced methods used belong to the {@link MathUtils} class created for this purpose. <br>
 * The None operator allows the user to deal with the different {@link QuaternaryMathOperation}s two by two, without
 * getting any {@link IndexOutOfBoundsException}. See {@link DerivativeTree} for more details
 * about signs dealing and branch splitting. <br>
 *
 * Note that the None operator might be removed in the future.
 *
 * @see BinaryOperationSign for other operations
 */
public enum QuaternaryOperationSign {

    DIFF_SUM('+', MathUtils::diffSum),
    DIFF_SUBTRACT('-', MathUtils::diffSubtract),
    DIFF_MULT('*', MathUtils::diffMult),
    DIFF_DIVIDE('/', MathUtils::diffDivide),
    DIFF_POW('^', MathUtils::diffPow),
    NONE(' ', null);

    private char sign;
    private QuaternaryMathOperation quaternaryFunction;

    QuaternaryOperationSign(char sign, QuaternaryMathOperation function) {
        this.sign = sign;
        this.quaternaryFunction = function;
    }

    public MathObject compute(MathObject a, MathObject a2, MathObject b, MathObject b2) {
        Objects.requireNonNull(quaternaryFunction);
        return quaternaryFunction.compute(a, a2, b, b2);
    }

    public static QuaternaryOperationSign getBySign(char sign) {
        for(QuaternaryOperationSign operation : values()) {
            if(operation.sign == sign) {
                return operation;
            }
        }
        return null;
    }

    @FunctionalInterface
    public interface QuaternaryMathOperation {
        MathObject compute(MathObject a, MathObject altA, MathObject b, MathObject altB);
    }
}
