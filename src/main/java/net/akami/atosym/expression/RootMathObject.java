package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;

import java.util.List;

public class RootMathObject extends LiteralFunction {

    public RootMathObject(List<MathObject> children, MaskContext context) {
        super(children, "root", 2, context);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.ROOT;
    }
}
