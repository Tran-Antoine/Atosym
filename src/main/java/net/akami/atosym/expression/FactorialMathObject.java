package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.FactorialDisplayer;

import java.util.Collections;

public class FactorialMathObject extends FunctionObject {

    private DisplayerVisitor displayer;

    public FactorialMathObject(MathObject child, MaskContext context) {
        super(Collections.singletonList(child), 1, context);
        this.displayer = new FactorialDisplayer(children);
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.FACTORIAL;
    }

    @Override
    public int priority() {
        return 3;
    }
}
