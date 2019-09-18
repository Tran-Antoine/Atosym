package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;

import java.util.List;

public class PowerMathObject extends MathFunction {

    public PowerMathObject(MathOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {

        if(children.size() != 2) throw new IllegalStateException("Power containing more or less than 2 elements");

        return DisplayUtils.join(children.get(0), children.get(1), "^");
    }
}
