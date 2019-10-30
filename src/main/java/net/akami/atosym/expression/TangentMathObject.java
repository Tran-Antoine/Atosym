package net.akami.atosym.expression;

import net.akami.atosym.core.MaskContext;

import java.util.List;

public class TangentMathObject extends LiteralFunction {

    public TangentMathObject(List<MathObject> children, MaskContext context) {
        super(children, "tan", 1, context);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.TAN;
    }
}
