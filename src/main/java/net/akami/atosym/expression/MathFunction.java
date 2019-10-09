package net.akami.atosym.expression;

import java.util.List;

public abstract class MathFunction implements MathObject {

    protected List<MathObject> children;
    private int size;

    public MathFunction(List<MathObject> children, int size) {
        this.children = children;
        this.size = size;
    }

    public void checkSize(int currentSize) {
        if(size != -1 && currentSize != size) {
            throw new IllegalStateException("Too few or too many parameters given");
        }
    }

    public void addChildrenTo(List<MathObject> list) {
        list.addAll(children);
    }

    public int getSize() {
        return size;
    }
}
