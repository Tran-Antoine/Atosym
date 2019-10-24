package net.akami.atosym.expression;

import java.util.List;

public class RootMathObject extends LiteralFunction {

    public RootMathObject(List<MathObject> children) {
        super(children, "root", 2);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.ROOT;
    }
}
