package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;

import java.util.List;

public abstract class MathFunction<T extends MathOperator> implements MathObject {

    protected T operator;
    protected List<MathObject> children;

    public MathFunction(T operator, List<MathObject> children) {
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

    protected int size() {
        return operator.getSize();
    }

    public void addChildrenTo(List<MathObject> list) {
        list.addAll(children);
    }
}
