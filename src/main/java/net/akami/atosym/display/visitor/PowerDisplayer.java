package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class PowerDisplayer extends SimpleDisplayerVisitor {

    private MathObject parent;

    public PowerDisplayer(List<MathObject> children, MathObject parent) {
        super(children, "pow");
        this.parent = parent;
    }

    // TODO : Change so that it works for a^b^c
    @Override
    public String accept(InfixNotationDisplayable displayable) {
        return DisplayUtils.join(children.get(0), children.get(1), "^", parent, displayable);

    }
}
