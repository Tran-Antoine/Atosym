package net.akami.atosym.expression;

import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.LiteralFunctionDisplayer;

import java.util.List;

public abstract class LiteralFunction extends FunctionObject {

    private DisplayerVisitor displayer;

    public LiteralFunction(List<MathObject> children, String displayName, int size) {
        super(children, size);
        this.displayer = new LiteralFunctionDisplayer(children, displayName);
    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }
}
