package net.akami.atosym.display;

import net.akami.atosym.expression.MathObject;

import java.util.List;

public class InfixNotationDisplayable extends SimpleDisplayable {

    // TODO : Add formatting tree, data, etc
    private MathObject parent;

    public InfixNotationDisplayable(List<MathObject> children, String functionName, MathObject parent) {
        super(children, functionName);
        this.parent = parent;
    }

    public MathObject getParent() {
        return parent;
    }
}
