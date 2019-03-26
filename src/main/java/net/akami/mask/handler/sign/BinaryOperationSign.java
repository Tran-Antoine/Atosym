package net.akami.mask.handler.sign;

import net.akami.mask.utils.MathUtils;

import java.util.Objects;

public enum BinaryOperationSign {

    SUM('+', MathUtils::sum, 0),
    SUBTRACT('-', MathUtils::subtract, 0),
    MULT('*', MathUtils::mult, 1),
    DIVIDE('/', MathUtils::divide, 1),
    POW('^', MathUtils::pow, 2),
    NONE(' ', null, 2);

    private char sign;
    private BinaryMathOperation binaryFunction;
    private int priorityLevel;

    BinaryOperationSign(char sign, BinaryMathOperation function, int priorityLevel) {
        this.sign = sign;
        this.binaryFunction = function;
        this.priorityLevel = priorityLevel;
    }

    public char getSign() {
        return sign;
    }

    public int getPriorityLevel() {
        return priorityLevel;
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
