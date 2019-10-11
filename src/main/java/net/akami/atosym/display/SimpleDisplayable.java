package net.akami.atosym.display;

import net.akami.atosym.expression.MathObject;

import java.util.List;

public class SimpleDisplayable implements Displayable {

    private List<MathObject> children;
    private String functionName;

    public SimpleDisplayable(List<MathObject> children, String functionName) {
        this.children = children;
        this.functionName = functionName;
    }

    public List<MathObject> getChildren() {
        return children;
    }

    public String getFunctionName() {
        return functionName;
    }
}
