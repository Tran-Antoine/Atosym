package net.akami.atosym.expression;

import net.akami.atosym.function.SineOperator;

import java.util.List;

public class SineMathObject extends LiteralFunction<SineOperator> {

    public SineMathObject(SineOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    protected int size() {
        return 1;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.SIN;
    }
}
