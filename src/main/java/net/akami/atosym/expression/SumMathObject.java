package net.akami.atosym.expression;

import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.SumDisplayer;

import java.util.List;

public class SumMathObject extends FunctionObject {

    private DisplayerVisitor displayer;

    public SumMathObject(List<MathObject> children) {
        super(children, -1);
        this.displayer = new SumDisplayer(children);
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.SUM;
    }

    @Override
    public int priority() {
        return 0;
    }
}
