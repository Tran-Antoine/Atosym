package net.akami.mask.math;

import net.akami.mask.utils.MathUtils;

public enum OperationSign {

    SUM('+', MathUtils::sum),
    SUBTRACT('-', MathUtils::subtract),
    MULT('*', MathUtils::mult),
    DIVIDE('/', MathUtils::divide),
    POW('^', MathUtils::pow),
    NONE(' ', null);

    private char sign;
    private MathOperation function;

    OperationSign(char sign, MathOperation function) {
        this.sign = sign;
        this.function = function;
    }

    public char getSign() {
        return sign;
    }

    public String compute(String a, String b) {
        return function.compute(a, b);
    }

    public static OperationSign getBySign(char sign) {
        for(OperationSign operation : values()) {
            if(operation.sign == sign) {
                return operation;
            }
        }
        return null;
    }

    private interface MathOperation {
        String compute(String a, String b);
    }
}
