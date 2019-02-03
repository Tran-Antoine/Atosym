package net.akami.mask;

import net.akami.mask.utils.MathUtils;

public enum Operation {

    SUM('+', MathUtils::sum),
    SUBTRACT('-', MathUtils::subtract),
    MULT('*', MathUtils::mult),
    DIVIDE('/', MathUtils::divide),
    POW('^', MathUtils::pow),
    NONE(' ', null);

    private char sign;
    private MathOperation function;

    Operation(char sign, MathOperation function) {
        this.sign = sign;
        this.function = function;
    }

    public char getSign() {
        return sign;
    }

    public float compute(String a, String b) {
        return function.compute(Float.parseFloat(a), Float.parseFloat(b));
    }

    public static Operation getBySign(char sign) {
        for(Operation operation : values()) {
            if(operation.sign == sign) {
                return operation;
            }
        }
        return null;
    }

    private interface MathOperation {
        float compute(float a, float b);
    }
}
