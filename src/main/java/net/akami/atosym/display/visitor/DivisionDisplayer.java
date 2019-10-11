package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class DivisionDisplayer extends SimpleDisplayerVisitor {

    @Override
    public String accept(InfixNotationDisplayable displayable) {
        MathObject parent = displayable.getParent();
        List<MathObject> children = displayable.getChildren();
        ((FunctionObject) parent).checkSize(children.size());
        return DisplayUtils.join(children.get(0), children.get(1), "/", parent, displayable);
    }
}
