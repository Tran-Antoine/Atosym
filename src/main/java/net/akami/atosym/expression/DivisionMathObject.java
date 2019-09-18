package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;

import java.util.List;

public class DivisionMathObject extends MathFunction {

    public DivisionMathObject(MathOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {

        if(children.size() != 2) throw new IllegalStateException("Division containing more or less than 2 elements");

        return DisplayUtils.join(children.get(0), children.get(1), "/");
    }
}
