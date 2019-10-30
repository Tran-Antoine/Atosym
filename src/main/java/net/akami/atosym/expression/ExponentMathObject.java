package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.PowerDisplayer;

import java.util.List;

public class ExponentMathObject extends FunctionObject {

    private DisplayerVisitor displayer;

    public ExponentMathObject(List<MathObject> children, MaskContext context) {
        super(children, -1, context);
        this.displayer = new PowerDisplayer(children, this);
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.POW;
    }

    @Override
    public int priority() {
        return 2;
    }
}
