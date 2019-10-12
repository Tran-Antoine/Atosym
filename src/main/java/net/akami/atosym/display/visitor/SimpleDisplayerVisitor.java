package net.akami.atosym.display.visitor;

import net.akami.atosym.display.FunctionalNotationDisplayable;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public abstract class SimpleDisplayerVisitor implements DisplayerVisitor {

    protected List<MathObject> children;
    protected String functionName;

    public SimpleDisplayerVisitor(List<MathObject> children, String functionName) {
        this.children = children;
        this.functionName = functionName;
    }

    @Override
    public String accept(FunctionalNotationDisplayable displayable) {
        String arguments = String.join(", ", displayable.toStringList(children, displayable));
        return functionName + DisplayUtils.surroundWithBrackets(arguments);
    }
}
