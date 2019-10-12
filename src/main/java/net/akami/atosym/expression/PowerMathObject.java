package net.akami.atosym.expression;

import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.PowerDisplayer;

import java.util.List;

public class PowerMathObject extends FunctionObject {

    private DisplayerVisitor displayer;

    public PowerMathObject(List<MathObject> children) {
        super(children, -1);
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
