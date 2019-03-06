package net.akami.mask.operation.sign;

import net.akami.mask.utils.MathUtils;

import java.util.Objects;

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

    public char getSign() {
        return sign;
    }

    public String compute(String a, String altA, String b, String altB) {
        Objects.requireNonNull(quaternaryFunction);
        return quaternaryFunction.compute(a, altA, b, altB);
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
        String compute(String a, String altA, String b, String altB);
    }
}
