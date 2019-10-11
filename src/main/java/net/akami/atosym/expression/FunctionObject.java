package net.akami.atosym.expression;

import java.util.List;

public abstract class FunctionObject implements MathObject {

    protected List<MathObject> children;
    private int size;

    public FunctionObject(List<MathObject> children, int size) {
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

    @Override
    public int compareTo(MathObject o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FunctionObject)) {
            return false;
        }

        FunctionObject object = (FunctionObject) obj;
        return getType() == object.getType() && children.equals(object.children);

    }

    public boolean childrenEqual(FunctionObject f2) {
        return children.equals(f2.children);
    }
}
