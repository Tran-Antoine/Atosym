package net.akami.atosym.expression;

import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class DivisionMathObject extends MathFunction {

    public DivisionMathObject(List<MathObject> children) {
        super(children, 2);
    }

    @Override
    public String display() {
        super.checkSize(children.size());
        return DisplayUtils.join(children.get(0), children.get(1), "/", this);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.DIV;
    }

    @Override
    public int priority() {
        return 1;
    }
}
