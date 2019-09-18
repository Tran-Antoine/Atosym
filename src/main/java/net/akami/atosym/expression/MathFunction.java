package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;

import java.util.List;

public abstract class MathFunction implements MathObject {

    protected MathOperator operator;
    protected List<MathObject> children;

    public MathFunction(MathOperator operator, List<MathObject> children) {
        this.operator = operator;
        this.children = children;
    }

    @Override
    public MathObject operate() {
        MathObject[] array = children.toArray(new MathObject[]{});
        return operator.rawOperate(array);
    }
}
