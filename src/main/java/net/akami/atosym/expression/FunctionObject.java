package net.akami.atosym.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FunctionObject implements MathObject {

    protected List<MathObject> children;
    private int size;

    public FunctionObject(List<MathObject> children, int size) {
        this.children = Collections.unmodifiableList(children);
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
    public boolean equals(Object obj) {
        if(!(obj instanceof FunctionObject)) {
            return false;
        }

        FunctionObject object = (FunctionObject) obj;
        return getType() == object.getType() && children.equals(object.children);

    }

    public List<MathObject> getChildren() {
        return children;
    }

    public List<MathObject> getFractionChildren(int start, int stop) {
        int realStart = start >= 0 ? start : children.size() + start;
        int realStop  = stop >= 0 ? stop : children.size() + stop;
        List<MathObject> newList = new ArrayList<>(stop - start + 2);
        for(int i = realStart; i <= realStop; i++) {
            newList.add(children.get(i));
        }
        return newList;
    }

    public MathObject getChild(int i) {
        int realI = i >= 0 ? i : children.size() + i;
        return children.get(realI);
    }
}
