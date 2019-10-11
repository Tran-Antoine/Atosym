package net.akami.atosym.expression;

import java.util.List;

public class CosineMathObject extends LiteralFunction {

    public CosineMathObject(List<MathObject> children) {
        super(children, "cos", 1);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.COS;
    }
}
