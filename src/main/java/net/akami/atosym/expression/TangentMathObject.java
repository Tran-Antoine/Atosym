package net.akami.atosym.expression;

import java.util.List;

public class TangentMathObject extends LiteralFunction {

    public TangentMathObject(List<MathObject> children) {
        super(children, "tan", 1);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.TAN;
    }
}
