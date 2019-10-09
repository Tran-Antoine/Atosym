package net.akami.atosym.expression;

import java.util.List;

public class SineMathObject extends LiteralFunction {

    public SineMathObject(List<MathObject> children) {
        super(children, "sin", 1);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.SIN;
    }
}
