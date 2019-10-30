package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.DivisionDisplayer;

import java.util.List;

public class DivisionMathObject extends FunctionObject {

    private DisplayerVisitor displayer;

    public DivisionMathObject(List<MathObject> children, MaskContext context) {
        super(children, 2, context);
        this.displayer = new DivisionDisplayer(children, this);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.DIV;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }
}
