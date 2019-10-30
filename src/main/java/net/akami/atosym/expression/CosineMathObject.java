package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;

import java.util.List;

public class CosineMathObject extends LiteralFunction {

    public CosineMathObject(List<MathObject> children, MaskContext context) {
        super(children, "cos", 1, context);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.COS;
    }
}
