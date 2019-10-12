package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class DivisionDisplayer extends SimpleDisplayerVisitor {

    private FunctionObject parent;

    public DivisionDisplayer(List<MathObject> children, FunctionObject parent) {
        super(children, "div");
        this.parent = parent;
    }

    @Override
    public String accept(InfixNotationDisplayable displayable) {
        parent.checkSize(children.size());
        return DisplayUtils.join(children.get(0), children.get(1), "/", parent, displayable);
    }
}
