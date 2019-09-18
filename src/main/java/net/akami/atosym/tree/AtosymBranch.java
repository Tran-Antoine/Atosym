package net.akami.atosym.tree;

import net.akami.atosym.expression.MathObject;

import java.util.List;

public class AtosymBranch {

    private List<AtosymBranch> children;
    private MathObject initialValue;
    private MathObject simplifiedValue;
    private float evalTime;

    public void merge() {
        long time = System.nanoTime();
        this.simplifiedValue = initialValue.operate();
        this.evalTime = (System.nanoTime() - time) / 1E9f;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public String toString() {
        return simplifiedValue.display();
    }
}
