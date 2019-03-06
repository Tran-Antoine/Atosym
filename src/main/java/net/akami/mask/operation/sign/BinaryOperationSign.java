package net.akami.mask.operation.sign;

import net.akami.mask.utils.MathUtils;

import java.util.Objects;

public enum BinaryOperationSign {

    SUM('+', MathUtils::sum),
    SUBTRACT('-', MathUtils::subtract),
    MULT('*', MathUtils::mult),
    DIVIDE('/', MathUtils::divide),
    POW('^', MathUtils::pow),
    NONE(' ', null);

    private char sign;
    private BinaryMathOperation binaryFunction;

    BinaryOperationSign(char sign, BinaryMathOperation function) {
        this.sign = sign;
        this.binaryFunction = function;
    }

    public char getSign() {
        return sign;
    }

    public String compute(String a, String b) {
        Objects.requireNonNull(binaryFunction);
        return binaryFunction.compute(a, b);
    }


    public static BinaryOperationSign getBySign(char sign) {
        for(BinaryOperationSign operation : values()) {
            if(operation.sign == sign) {
                return operation;
            }
        }
        return null;
    }

    @FunctionalInterface
    private interface BinaryMathOperation {
        String compute(String a, String b);
    }
}
