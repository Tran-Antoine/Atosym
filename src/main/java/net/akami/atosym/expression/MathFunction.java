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
        checkSize(children.size());
        return operator.rawOperate(children);
    }

    public void checkSize(int size) {
        int rightSize = size();
        if(rightSize != -1 && size != rightSize) {
            throw new IllegalStateException("Too few or too many parameters given");
        }
    }

    protected abstract int size();
}
