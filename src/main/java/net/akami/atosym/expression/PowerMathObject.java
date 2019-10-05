package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class PowerMathObject extends MathFunction {

    public PowerMathObject(MathOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {
        return DisplayUtils.join(children.get(0), children.get(1), "^");
    }

    @Override
    protected int size() {
        return -1;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.POW;
    }
}
