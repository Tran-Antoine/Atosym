package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;

import java.util.List;

public class DivisionMathObject extends MathFunction {

    public DivisionMathObject(MathOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {
        super.checkSize(children.size());
        return DisplayUtils.join(children.get(0), children.get(1), "/");
    }

    @Override
    protected int size() {
        // a/b/c should become a/(bc)
        return 2;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.DIV;
    }
}
