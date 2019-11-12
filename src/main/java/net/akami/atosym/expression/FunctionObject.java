package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.display.InfixNotationDisplayable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FunctionObject implements MathObject {

    protected List<MathObject> children;
    private int size;

    public FunctionObject(List<MathObject> children, int size, MaskContext context) {
        MathObjectType type = getType();
        if(type.hasProperty(MathProperty.COMMUTATIVITY)) {
            children.sort(context.getSortingRules(type));
        }
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
        if(getType() != object.getType()) return false;

        return children.equals(object.children) ||
                (getType().hasProperty(MathProperty.COMMUTATIVITY) && commutativityEquality(children, object.children));
    }

    public static boolean commutativityEquality(List<MathObject> l1, List<MathObject> l2) {
        return l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1);
    }

    public List<MathObject> getChildren() {
        return children;
    }

    public List<MathObject> getChildrenFraction(int start, int stop) {
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

    @Override
    public String toString() {
        return getDisplayer().accept(InfixNotationDisplayable.EMPTY_INSTANCE);
    }
}
