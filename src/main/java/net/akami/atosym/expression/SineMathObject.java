package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;

import java.util.List;

public class SineMathObject extends LiteralFunction {

    public SineMathObject(List<MathObject> children, MaskContext context) {
        super(children, "sin", 1, context);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.SIN;
    }
}
