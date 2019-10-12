package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class LiteralFunctionDisplayer extends SimpleDisplayerVisitor {

    public LiteralFunctionDisplayer(List<MathObject> children, String functionName) {
        super(children, functionName);
    }

    // Infix notation is the same as functional notation for literal functions
    @Override
    public String accept(InfixNotationDisplayable displayable) {
        String arguments = String.join(", ", displayable.toStringList(children, displayable));
        return functionName + DisplayUtils.surroundWithBrackets(arguments);
    }
}
