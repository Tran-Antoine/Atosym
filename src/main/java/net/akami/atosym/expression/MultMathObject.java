package net.akami.atosym.expression;

import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.MultDisplayer;

import java.util.List;

public class MultMathObject extends FunctionObject {

    private DisplayerVisitor displayer;

    public MultMathObject(List<MathObject> children) {
        super(children, -1);
        this.displayer = new MultDisplayer(children);
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.MULT;
    }

    @Override
    public int priority() {
        return 1;
    }
}
