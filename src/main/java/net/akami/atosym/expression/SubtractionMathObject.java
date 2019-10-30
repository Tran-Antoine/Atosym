package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.display.visitor.SubtractionDisplayer;

import java.util.List;

public class SubtractionMathObject extends FunctionObject {

    private DisplayerVisitor displayer;

    public SubtractionMathObject(List<MathObject> children, MaskContext context) {
        // a-b-c should become a - (b+c) -> sub(a, sum(b, c)), therefore the size is always 2
        super(children, 2, context);
        this.displayer = new SubtractionDisplayer(children, this);
    }

    @Override
    public DisplayerVisitor getDisplayer() {
        return displayer;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.SUB;
    }

    @Override
    public int priority() {
        return 0;
    }

}
